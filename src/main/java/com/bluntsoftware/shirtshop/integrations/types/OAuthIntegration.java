package com.bluntsoftware.shirtshop.integrations.types;

import java.util.Date;
import java.util.Map;

public interface OAuthIntegration {
    String getAuthUrl();
    String getType();
    //long getExpirationMillis(Map<String, Object> credentials);
    Date getExpiresAt(Map<String, Object> credentials);
    Map<String, Object> getCredentials(Map<String, Object> authCode);
    Map<String, Object> refreshCredentials(Map<String, Object> credentials);
}
