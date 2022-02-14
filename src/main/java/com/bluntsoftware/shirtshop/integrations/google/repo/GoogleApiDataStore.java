package com.bluntsoftware.shirtshop.integrations.google.repo;

import com.bluntsoftware.shirtshop.integrations.Integration;
import com.bluntsoftware.shirtshop.integrations.IntegrationService;

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
    private final IntegrationService integrationService;
    private final Lock lock = new ReentrantLock();

    protected GoogleApiDataStore(DataStoreFactory dataStoreFactory, String id,   IntegrationService integrationService){//}, AuthorizationUtil authorizationUtil) {
        super(dataStoreFactory, id);
        this.integrationService = integrationService;

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
                Integration user =  this.integrationService.get(key);
                if (user != null && user.getStoredCredential() != null) {
                    return IOUtils.deserialize(user.getStoredCredential());
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
                Integration user =  this.integrationService.get(key);
                if (user == null) {
                    user =  Integration.builder().id(key).build();
                }
                user.setStoredCredential(IOUtils.serialize(value));
                integrationService.save(user);
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
