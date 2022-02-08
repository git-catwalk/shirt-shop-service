package com.bluntsoftware.shirtshop.google.repo;

import com.bluntsoftware.shirtshop.service.CompanyService;
import com.google.api.client.util.store.AbstractDataStoreFactory;
import com.google.api.client.util.store.DataStore;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;

@Component
public class GoogleApiDataStoreFactory extends AbstractDataStoreFactory {

    private final CompanyService companyService;

    public GoogleApiDataStoreFactory(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Override
    protected <V extends Serializable> DataStore<V> createDataStore(String id) throws IOException {
        return new GoogleApiDataStore<V>(this, id, companyService);
    }
}
