package com.bluntsoftware.shirtshop.integrations.types.square.oauth;

import com.bluntsoftware.shirtshop.integrations.types.AbstractOAuthIntegration;
import com.bluntsoftware.shirtshop.integrations.types.square.config.SquareConfig;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
/*
https://developer.squareup.com/docs/oauth-api/create-urls-for-square-authorization
 */
public class SquareOAuthIntegration extends AbstractOAuthIntegration {

    private final SquareConfig squareConfig;
    public static final String SQUARE_ID = "square";
    private static final String SQUARE_STATE = "state-443434";

    public SquareOAuthIntegration(SquareConfig squareConfig) {
        this.squareConfig = squareConfig;
    }

    @Override
    public String getAuthUrl() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(squareConfig.getAuthUrl())
                .queryParam("client_id",squareConfig.getClientId())
                .queryParam("scope","CUSTOMERS_WRITE+CUSTOMERS_READ+MERCHANT_PROFILE_READ+ORDERS_WRITE+ORDERS_READ+INVOICES_WRITE+INVOICES_READ")
                .queryParam("session","false")
                //.queryParam("response_type","code")
                .queryParam("state",SQUARE_STATE)
                .queryParam("redirect_uri",squareConfig.getRedirectUrl())
                .buildAndExpand();
        return uriComponents.toUriString();
    }

    @Override
    public String getType() {
        return SQUARE_ID;
    }

    @Override
    public Date getExpiresAt(Map<String, Object> credentials) {
        if(credentials.containsKey("expires_at")){
            String expiresAt = credentials.get("expires_at").toString();
            ZonedDateTime result = ZonedDateTime.parse(expiresAt, DateTimeFormatter.ISO_DATE_TIME);
            LocalDateTime  ldt = result.toLocalDateTime();
            return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        }
        return null;
    }

    @Override
    public Map<String, Object> getCredentials(Map<String, Object> authCode) {
        if(!authCode.get("state").toString().equalsIgnoreCase(SQUARE_STATE)){
            throw new RuntimeException("state mismatch");
        }
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("code",authCode.get("code").toString());
        data.add("grant_type","authorization_code");
        data.add("redirect_uri",squareConfig.getRedirectUrl());
        data.add("client_id",squareConfig.getClientId());
        data.add("client_secret",squareConfig.getClientSecret());
        Map<String, Object> ret = authorize(data);

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

    public Map<String, Object> authorize(MultiValueMap<String, String> data) {
        HttpHeaders headers = createHeaders(squareConfig.getClientId(),squareConfig.getClientSecret());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(data,headers);
        ResponseEntity<Map> response = restTemplate().exchange(squareConfig.getTokenUrl(), HttpMethod.POST, request, Map.class );
        return response.getBody();
    }
}
