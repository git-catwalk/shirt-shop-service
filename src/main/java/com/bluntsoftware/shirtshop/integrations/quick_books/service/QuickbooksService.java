package com.bluntsoftware.shirtshop.integrations.quick_books.service;

import com.bluntsoftware.shirtshop.integrations.AuthResponse;
import com.bluntsoftware.shirtshop.integrations.Integration;
import com.bluntsoftware.shirtshop.integrations.IntegrationService;
import com.bluntsoftware.shirtshop.integrations.quick_books.config.QuickbooksConfig;
import com.bluntsoftware.shirtshop.tenant.TenantResolver;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Service
public class QuickbooksService {
    private final IntegrationService integrationService;
    private final QuickbooksConfig qbConfig;
    private static final String QB_STATE = "state-387465";
    private static final String INTEGRATION_ID = "quick-books";

    public QuickbooksService(IntegrationService integrationService, QuickbooksConfig qbConfig) {
        this.integrationService = integrationService;
        this.qbConfig = qbConfig;
    }

    public Map<String,String> authUrl(){
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(qbConfig.getAuthUrl())
                .queryParam("client_id",qbConfig.getClientId())
                .queryParam("scope","com.intuit.quickbooks.payment")
                .queryParam("response_type","code")
                .queryParam("state",QB_STATE)
                .queryParam("redirect_uri",qbConfig.getRedirectUrl())
                .buildAndExpand();

        Map<String,String> ret = new HashMap<>();
        ret.put("url",uriComponents.toUriString());
        return ret;
    }

    public ResponseEntity<?> token(AuthResponse auth){
        if(!auth.getState().equalsIgnoreCase(QB_STATE)){
            throw new RuntimeException("state mismatch");
        }

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = createHeaders(qbConfig.getClientId(),qbConfig.getClientSecret());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();

        data.add("code",auth.getCode());
        data.add("grant_type","authorization_code");
        data.add("redirect_uri",qbConfig.getRedirectUrl());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(data,headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(qbConfig.getTokenUrl(), HttpMethod.POST, request, Map.class );
            this.integrationService.save(Integration.builder()
                    .id(INTEGRATION_ID)
                    .tenant(TenantResolver.resolve())
                    .credentials(response.getBody())
                    .build());
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    private HttpHeaders createHeaders(String username, String password){
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")) );
            String authHeader = "Basic " + new String( encodedAuth );
            set( "Authorization", authHeader );
        }};
    }
}
