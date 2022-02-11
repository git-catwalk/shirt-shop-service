package com.bluntsoftware.shirtshop.integrations.quick_books.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Data
@Component
@ConfigurationProperties(prefix="qb.api.oauth2")
public class QuickbooksOauthConfig {
    String tokenUrl;
    String authUrl;
    String clientId;
    String clientSecret;
    String redirectUrl;
}
