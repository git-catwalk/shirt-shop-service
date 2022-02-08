package com.bluntsoftware.shirtshop.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix="google.api.oauth2")
public class GoogleConfig {
    String tokenUrl;
    String authUrl;
    String clientId;
    String clientSecret;
}
