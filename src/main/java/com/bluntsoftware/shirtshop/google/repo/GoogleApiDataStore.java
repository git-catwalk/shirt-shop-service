package com.bluntsoftware.shirtshop.google.repo;

import com.bluntsoftware.shirtshop.google.model.GoogleApi;
import com.bluntsoftware.shirtshop.model.Company;
import com.bluntsoftware.shirtshop.service.CompanyService;
import com.google.api.client.util.IOUtils;
import com.google.api.client.util.store.AbstractDataStore;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GoogleApiDataStore <V extends Serializable> extends AbstractDataStore<V> {
    private final CompanyService companyService;
    private final Lock lock = new ReentrantLock();

    protected GoogleApiDataStore(DataStoreFactory dataStoreFactory, String id, CompanyService companyService){//}, AuthorizationUtil authorizationUtil) {
        super(dataStoreFactory, id);
        this.companyService = companyService;
    }

    @Override
    public Set<String> keySet() throws IOException {
        return null;
    }

    @Override
    public Collection<V> values() throws IOException {
        return null;
    }

    @Override
    public V get(String key) throws IOException {
        if (key != null) {
            lock.lock();
            try {
                Company user = companyService.get().get();

                if (user.getId().equals(key)) {
                    GoogleApi googleApi = user.getGoogleApi();

                    if (googleApi != null && googleApi.getStoredCredential() != null) {
                        return IOUtils.deserialize(googleApi.getStoredCredential());
                    }
                }
            } finally {
                lock.unlock();
            }
        }
        return null;
    }


    @Override
    public DataStore<V> set(String key, V value) throws IOException {
        if (key != null) {
            lock.lock();

            try {
                Company user =  companyService.get().get();

                if (user.getId().equals(key)) {
                    GoogleApi googleApi = user.getGoogleApi();
                    if (googleApi == null ) googleApi = new GoogleApi();

                    googleApi.setStoredCredential(IOUtils.serialize(value));

                    user.setGoogleApi(googleApi);

                    companyService.save(user);
                }
            } finally {
                lock.unlock();
            }
        }

        return this;
    }


    @Override
    public DataStore<V> clear() throws IOException {
        return null;
    }


    @Override
    public DataStore<V> delete(String key) throws IOException {
        return null;
    }
}
