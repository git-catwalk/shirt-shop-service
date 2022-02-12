package com.bluntsoftware.shirtshop.integrations.quick_books.service;

import com.bluntsoftware.shirtshop.integrations.AuthResponse;
import com.bluntsoftware.shirtshop.integrations.Integration;
import com.bluntsoftware.shirtshop.integrations.IntegrationService;
import com.bluntsoftware.shirtshop.integrations.quick_books.config.QuickbooksApiConfig;
import com.bluntsoftware.shirtshop.integrations.quick_books.config.QuickbooksOauthConfig;
import com.bluntsoftware.shirtshop.tenant.TenantResolver;
import com.intuit.ipp.core.Context;
import com.intuit.ipp.core.ServiceType;
import com.intuit.ipp.exception.FMSException;
import com.intuit.ipp.security.OAuth2Authorizer;
import com.intuit.ipp.services.DataService;
import com.intuit.ipp.util.Config;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class QuickbooksAuthService implements AuthService {
    private final IntegrationService integrationService;
    private final QuickbooksOauthConfig qbConfig;
    private final QuickbooksApiConfig qbApiConfig;
    private static final String QB_STATE = "state-387465";
    private static final String INTEGRATION_ID = "quick-books";

    public QuickbooksAuthService(IntegrationService integrationService, QuickbooksOauthConfig qbConfig, QuickbooksApiConfig qbApiConfig) {
        this.integrationService = integrationService;
        this.qbConfig = qbConfig;
        this.qbApiConfig = qbApiConfig;
    }

    public Map<String,String> authUrl(){
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(qbConfig.getAuthUrl())
                .queryParam("client_id",qbConfig.getClientId())
                .queryParam("scope","com.intuit.quickbooks.accounting")
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
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("code",auth.getCode());
        data.add("grant_type","authorization_code");
        data.add("redirect_uri",qbConfig.getRedirectUrl());
        return authorize(data,auth.getRealmId());
    }
    /*
       System.out.println("Expires on " + integration.getExpires());
       System.out.println("Current Date " + new Date());
    */
    @Override
    public boolean credentialsExpired() {
        Integration integration = this.integrationService.get(INTEGRATION_ID);
        return integration.getExpires().compareTo(new Date()) < 0;
    }

    @Override
    public ResponseEntity<?> refreshCredentials() {
        Integration integration = getCredentials();
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("grant_type","refresh_token");
        data.add("refresh_token",integration.getCredentials().get("refresh_token").toString());
        return authorize(data,integration.getRealmId());
    }

    @Override
    public boolean hasCredentials() {
        return this.integrationService.get(INTEGRATION_ID) != null;
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

    public Integration getCredentials() {
        return this.integrationService.get(INTEGRATION_ID);
    }

    public DataService getDataService()   {
        Config.setProperty(Config.BASE_URL_QBO,  qbApiConfig.getUrl() + "/v3/company");
        if(credentialsExpired()){
            refreshCredentials();
        }
          Integration integration = getCredentials();
          String accessToken = integration.getCredentials().get("access_token").toString();
          OAuth2Authorizer authorizer = new OAuth2Authorizer(accessToken);
        Context context = null;
        try {
            context = new Context(authorizer, ServiceType.QBO, integration.getRealmId());
        } catch (FMSException e) {
            e.printStackTrace();
        }
        return new DataService(context);
    }

    private ResponseEntity<?> authorize(MultiValueMap<String, String> data,String realmId){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = createHeaders(qbConfig.getClientId(),qbConfig.getClientSecret());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(data,headers);
        try {
            ResponseEntity<Map> response = restTemplate.exchange(qbConfig.getTokenUrl(), HttpMethod.POST, request, Map.class );
            this.integrationService.save(get(response,realmId));
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    private Integration get(ResponseEntity<Map> response,String realmId){
        long expiresInMillis = Long.parseLong(response.getBody().get("expires_in").toString()) * 1000;
        return Integration.builder()
                .id(INTEGRATION_ID)
                .issued(new Date())
                .expires(new Date(System.currentTimeMillis() + expiresInMillis))
                .tenant(TenantResolver.resolve())
                .realmId(realmId)
                .credentials(response.getBody())
                .build();
    }
}
