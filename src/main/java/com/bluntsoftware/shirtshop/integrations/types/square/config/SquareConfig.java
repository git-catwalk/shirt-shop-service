package com.bluntsoftware.shirtshop.integrations.types.square.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
@Primary
@Data
@Component
@ConfigurationProperties(prefix="square.api.oauth2")
public class SquareConfig {
        String tokenUrl;
        String authUrl;
        String clientId;
        String clientSecret;
        String redirectUrl;
        String sandboxToken;
}
