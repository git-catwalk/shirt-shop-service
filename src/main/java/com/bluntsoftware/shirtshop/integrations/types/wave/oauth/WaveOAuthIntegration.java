package com.bluntsoftware.shirtshop.integrations.types.wave.oauth;

import com.bluntsoftware.shirtshop.integrations.types.AbstractOAuthIntegration;

import com.bluntsoftware.shirtshop.integrations.types.wave.config.WaveConfig;

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

public class WaveOAuthIntegration extends AbstractOAuthIntegration {
    private final WaveConfig waveConfig;
    public static final String WAVE_ID = "wave";
    private static final String WAVE_STATE = "state-387465";
    public WaveOAuthIntegration(WaveConfig waveConfig) {
        this.waveConfig = waveConfig;
    }

    @Override
    public String getAuthUrl() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(waveConfig.getAuthUrl())
                .queryParam("client_id",waveConfig.getClientId())
                .queryParam("scope","business:* invoice:*")
                .queryParam("response_type","code")
                .queryParam("state",WAVE_STATE)
                .queryParam("redirect_uri",waveConfig.getRedirectUrl())
                .buildAndExpand();
        return uriComponents.toUriString();
    }

    @Override
    public Map<String, Object> getCredentials(Map<String, Object> authCode) {

        if(!authCode.get("state").toString().equalsIgnoreCase(WAVE_STATE)){
            throw new RuntimeException("state mismatch");
        }

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("client_id",waveConfig.getClientId());
        data.add("client_secret",waveConfig.getClientSecret());
        data.add("code",authCode.get("code").toString());
        data.add("grant_type","authorization_code");
        data.add("redirect_uri",waveConfig.getRedirectUrl());
        return authorize(data);

    }

    @Override
    public Map<String, Object> refreshCredentials(Map<String, Object> credentials) {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("grant_type","refresh_token");
        data.add("refresh_token",credentials.get("refresh_token").toString());
        return authorize(data);
    }

    @Override
    public long getExpirationMillis(Map<String, Object> credentials) {
        return Long.parseLong(credentials.get("expires_in").toString()) * 1000;
    }

    private Map<String,Object> authorize(MultiValueMap<String, String> data){
        HttpHeaders headers = createHeaders(waveConfig.getClientId(),waveConfig.getClientSecret());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(data,headers);
        ResponseEntity<Map> response = restTemplate().exchange(waveConfig.getTokenUrl(), HttpMethod.POST, request, Map.class );
        return response.getBody();
    }

    @Override
    public String getType() {
        return WAVE_ID;
    }

}
