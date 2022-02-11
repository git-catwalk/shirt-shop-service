package com.bluntsoftware.shirtshop.integrations.wave.controller;

import com.bluntsoftware.shirtshop.integrations.wave.config.WaveConfig;
import com.bluntsoftware.shirtshop.integrations.AuthResponse;

import com.bluntsoftware.shirtshop.integrations.wave.service.WaveCustomerService;
import com.bluntsoftware.shirtshop.service.CompanyService;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rest/wave")
public class WaveController {
    private final CompanyService companyService;
    private final WaveCustomerService waveCustomerService;
    private final WaveConfig waveConfig;
    private static final String WAVE_STATE = "state-387465";
    public WaveController(CompanyService companyService, WaveCustomerService waveCustomerService, WaveConfig waveConfig) {
        this.companyService = companyService;
        this.waveCustomerService = waveCustomerService;
        this.waveConfig = waveConfig;
    }

    @GetMapping(value = "/customer",produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,String> get(){
        return this.waveCustomerService.getBusinesses();
    }

    @GetMapping(value = "/authorizationCode",produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,String> authorize(){
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(waveConfig.getAuthUrl())
                .queryParam("client_id",waveConfig.getClientId())
                .queryParam("scope","business:* invoice:*")
                .queryParam("response_type","code")
                .queryParam("state",WAVE_STATE)
                .queryParam("redirect_uri",waveConfig.getRedirectUrl())
                .buildAndExpand();

        Map<String,String> ret = new HashMap<>();
        ret.put("url",uriComponents.toUriString());
        return ret;
    }

    @PostMapping("/token")
    public ResponseEntity<?> createToken(@RequestBody AuthResponse auth) {

        if(!auth.getState().equalsIgnoreCase(WAVE_STATE)){
            throw new RuntimeException("state mismatch");
        }

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = createHeaders(waveConfig.getClientId(),waveConfig.getClientSecret());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("client_id",waveConfig.getClientId());
        data.add("client_secret",waveConfig.getClientSecret());
        data.add("code",auth.getCode());
        data.add("grant_type","authorization_code");
        data.add("redirect_uri",waveConfig.getRedirectUrl());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(data,headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(waveConfig.getTokenUrl(), HttpMethod.POST, request, Map.class );
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    HttpHeaders createHeaders(String username, String password){
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")) );
            String authHeader = "Basic " + new String( encodedAuth );
            set( "Authorization", authHeader );
        }};
    }
}
