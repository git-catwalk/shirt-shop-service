package com.bluntsoftware.shirtshop.integrations.types.google.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
@Primary
@Data
@Component
@ConfigurationProperties(prefix="google.api.oauth2")
public class GoogleConfig {
    String tokenUrl;
    String authUrl;
    String clientId;
    String clientSecret;
    String redirectUrl;
}
