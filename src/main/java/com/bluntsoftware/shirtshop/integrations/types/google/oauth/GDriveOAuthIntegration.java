package com.bluntsoftware.shirtshop.integrations.types.google.oauth;

import com.bluntsoftware.shirtshop.integrations.types.AbstractOAuthIntegration;
import com.bluntsoftware.shirtshop.integrations.types.google.config.GoogleConfig;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Map;

public class GDriveOAuthIntegration extends AbstractOAuthIntegration {
    public static final String GOOGLE_DRIVE_ID = "gDrive";
    private final GoogleConfig googleConfig;
    private static final String GOOGLE_STATE = "state-387465";

    public GDriveOAuthIntegration(GoogleConfig googleConfig) {
        this.googleConfig = googleConfig;
    }

    @Override
    public String getAuthUrl() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(googleConfig.getAuthUrl())
                .queryParam("access_type","offline")
                .queryParam("client_id",googleConfig.getClientId())
                .queryParam("scope","https://www.googleapis.com/auth/drive")
                .queryParam("response_type","code")
                .queryParam("state",GOOGLE_STATE)
                .queryParam("redirect_uri",googleConfig.getRedirectUrl())
                .buildAndExpand();
        return uriComponents.toString();
    }

    @Override
    public Map<String, Object> getCredentials(Map<String, Object> authCode) {
        if(authCode.get("state") != null && !authCode.get("state").toString().equalsIgnoreCase(GOOGLE_STATE)){
            throw new RuntimeException("state mismatch");
        }
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("code",authCode.get("code").toString());
        data.add("grant_type","authorization_code");
        data.add("redirect_uri",googleConfig.getRedirectUrl());
        return authorize(data);
    }

    @Override
    public Map<String, Object> refreshCredentials(Map<String, Object> credentials) {
        String refreshToken = credentials.get("refresh_token").toString();
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("grant_type","refresh_token");
        data.add("refresh_token",refreshToken);
        Map<String, Object> ret = authorize(data);
        ret.put("refresh_token",refreshToken);
        return ret;
    }

    @Override
    public String getType() {
        return GOOGLE_DRIVE_ID;
    }

    @Override
    public long getExpirationMillis(Map<String, Object> credentials) {
        return Long.parseLong(credentials.get("expires_in").toString()) * 1000;
    }

    private Map<String,Object> authorize(MultiValueMap<String, String> data){
        HttpHeaders headers = createHeaders(googleConfig.getClientId(),googleConfig.getClientSecret());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(data,headers);
        ResponseEntity<Map> response = restTemplate().exchange(googleConfig.getTokenUrl(), HttpMethod.POST, request, Map.class );
        return response.getBody();
    }
}
