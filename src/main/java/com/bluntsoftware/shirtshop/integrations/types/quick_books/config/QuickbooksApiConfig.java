package com.bluntsoftware.shirtshop.integrations.types.quick_books.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Data
@Component
@ConfigurationProperties(prefix="qb.api")
public class QuickbooksApiConfig {
    String url;
}
