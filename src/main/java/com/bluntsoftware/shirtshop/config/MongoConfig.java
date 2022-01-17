package com.bluntsoftware.shirtshop.config;

import com.bluntsoftware.shirtshop.tenant.TenantMongoDbFactory;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mongo.MongoClientFactory;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoDatabaseFactorySupport;
import java.util.stream.Collectors;

@Configuration
public class MongoConfig {
    @Value("${spring.data.mongodb.database}")
    String dbName;
    @Bean
    MongoDatabaseFactorySupport<MongoClient> mongoDbFactory(MongoClient client){
        return new TenantMongoDbFactory(client,dbName);
    }

    @Bean
    public MongoClient mongo(ObjectProvider<MongoClientSettingsBuilderCustomizer> builderCustomizers,
                             MongoClientSettings settings) {
        return new MongoClientFactory(builderCustomizers.orderedStream().collect(Collectors.toList()))
                .createMongoClient(settings);
    }

    @Bean
    MongoClientSettings mongoClientSettings() {
        return MongoClientSettings.builder().build();
    }
}
