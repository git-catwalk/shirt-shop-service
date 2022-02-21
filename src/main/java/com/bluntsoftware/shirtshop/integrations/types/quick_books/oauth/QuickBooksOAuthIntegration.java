package com.bluntsoftware.shirtshop.integrations.types.quick_books.oauth;

import com.bluntsoftware.shirtshop.integrations.types.AbstractOAuthIntegration;
import com.bluntsoftware.shirtshop.integrations.types.quick_books.config.QuickbooksOauthConfig;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Map;

public class QuickBooksOAuthIntegration extends AbstractOAuthIntegration {
    private final QuickbooksOauthConfig  qbConfig;
    public static final String QUICK_BOOKS_ID = "quick-books";
    private static final String QB_STATE = "state-387465";

    public QuickBooksOAuthIntegration(QuickbooksOauthConfig qbConfig) {
        this.qbConfig = qbConfig;
    }

    @Override
    public String getAuthUrl() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(qbConfig.getAuthUrl())
                .queryParam("client_id",qbConfig.getClientId())
                .queryParam("scope","com.intuit.quickbooks.accounting")
                .queryParam("response_type","code")
                .queryParam("state",QB_STATE)
                .queryParam("redirect_uri",qbConfig.getRedirectUrl())
                .buildAndExpand();
        return uriComponents.toUriString();
    }

    @Override
    public Map<String, Object> getCredentials(Map<String, Object> authCode) {
        if(!authCode.get("state").toString().equalsIgnoreCase(QB_STATE)){
            throw new RuntimeException("state mismatch");
        }
        String realmId = authCode.get("realmId").toString();
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("code",authCode.get("code").toString());
        data.add("grant_type","authorization_code");
        data.add("redirect_uri",qbConfig.getRedirectUrl());
        Map<String, Object> ret = authorize(data);
        ret.put("realm_id",realmId);
        return ret;
    }

    @Override
    public Map<String, Object> refreshCredentials(Map<String, Object> credentials) {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("grant_type","refresh_token");
        data.add("refresh_token",credentials.get("refresh_token").toString());
        Map<String, Object> ret = authorize(data);
        ret.put("realm_id",credentials.get("realm_id").toString());
        return ret;
    }

    @Override
    public String getType() {
        return QUICK_BOOKS_ID;
    }

    @Override
    public long getExpirationMillis(Map<String, Object> credentials) {
        return Long.parseLong(credentials.get("expires_in").toString()) * 1000;
    }

    private Map<String,Object> authorize(MultiValueMap<String, String> data){
        HttpHeaders headers = createHeaders(qbConfig.getClientId(),qbConfig.getClientSecret());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(data,headers);
        ResponseEntity<Map> response = restTemplate().exchange(qbConfig.getTokenUrl(), HttpMethod.POST, request, Map.class );
        return response.getBody();
    }
}
