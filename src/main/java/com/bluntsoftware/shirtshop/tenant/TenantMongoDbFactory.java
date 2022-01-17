package com.bluntsoftware.shirtshop.tenant;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

public class TenantMongoDbFactory extends SimpleMongoClientDatabaseFactory {

    public TenantMongoDbFactory(MongoClient mongoClient, String databaseName) {
        super(mongoClient, databaseName);
    }

    @NotNull
    @Override
    public MongoDatabase getMongoDatabase(@NotNull String dbName) throws DataAccessException {
        dbName += "_" + TenantResolver.resolve();
        return super.getMongoDatabase(dbName);
    }
}
