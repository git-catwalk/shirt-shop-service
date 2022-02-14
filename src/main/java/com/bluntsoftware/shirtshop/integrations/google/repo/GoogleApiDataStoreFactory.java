package com.bluntsoftware.shirtshop.integrations.google.repo;

import com.bluntsoftware.shirtshop.integrations.IntegrationService;
import com.bluntsoftware.shirtshop.service.CompanyService;
import com.google.api.client.util.store.AbstractDataStoreFactory;
import com.google.api.client.util.store.DataStore;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;

@Component
public class GoogleApiDataStoreFactory extends AbstractDataStoreFactory {

    private final IntegrationService integrationService;

    public GoogleApiDataStoreFactory(IntegrationService integrationService) {
        this.integrationService = integrationService;
    }

    @Override
    protected <V extends Serializable> DataStore<V> createDataStore(String id) throws IOException {
        return new GoogleApiDataStore<V>(this, id, integrationService);
    }
}
