package com.bluntsoftware.shirtshop.integrations.google.util;

import com.bluntsoftware.shirtshop.integrations.google.config.GoogleConfig;
import com.bluntsoftware.shirtshop.integrations.google.model.Constants;
import com.bluntsoftware.shirtshop.integrations.google.model.GoogleAuthResponse;
import com.bluntsoftware.shirtshop.integrations.google.repo.GoogleApiDataStoreFactory;
import com.google.api.client.auth.oauth2.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.services.drive.DriveScopes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class GoogleOauthUtil {
    private static final Logger LOG = LoggerFactory.getLogger(GoogleOauthUtil.class);
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private final GoogleConfig googleConfig;

    private DataStore<StoredCredential> dataStore;

    @Autowired
    public GoogleOauthUtil(GoogleApiDataStoreFactory dataStoreFactory, GoogleConfig googleConfig) {
        this.googleConfig = googleConfig;
        setupDataStore(dataStoreFactory);
    }

    private void setupDataStore(DataStoreFactory dataStoreFactory) {
        try {
            dataStore = dataStoreFactory.getDataStore(Constants.CREDENTIAL_STORE_ID);
        } catch (Exception e) {
            LOG.error("Problem creating data store for credentials!", e);
        }
    }

    private List<CredentialRefreshListener> getListeners(String userId) {
        DataStoreCredentialRefreshListener listener = new DataStoreCredentialRefreshListener(userId, dataStore);
        List<CredentialRefreshListener> listeners = new ArrayList<>();
        listeners.add(listener);
        return listeners;
    }

    private GoogleAuthorizationCodeFlow getAuthorizationCodeFlow(String userId) throws IOException, GeneralSecurityException {
        List<CredentialRefreshListener> listeners = getListeners(userId);
        NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory gsonFactory = new GsonFactory();
        return new GoogleAuthorizationCodeFlow.Builder(
                transport,
                gsonFactory,
                this.googleConfig.getClientId(),
                this.googleConfig.getClientSecret(),
                SCOPES)
                .setCredentialDataStore(dataStore)
                .setAccessType("offline")
                .setRefreshListeners(listeners)
                .build();
    }

    public Credential getCredential(String companyId) {
        Credential credential = null;
        try {
            GoogleAuthorizationCodeFlow acf = getAuthorizationCodeFlow(companyId);
            credential = acf.loadCredential(companyId);
        } catch (Exception e) {
            LOG.error("Problem retrieving credential!", e);
        }
        return credential;
    }

    public String getAuthorizationCodeUrl(String id, String redirectUrl) {
        String url = null;

        try {
            AuthorizationCodeRequestUrl acru = getAuthorizationCodeFlow(id).newAuthorizationUrl();
            acru.setRedirectUri(redirectUrl).build();
            acru.setState("state");

            LOG.debug(acru.toString());

            url = acru.build();
        } catch (Exception e) {
            LOG.error("Problem getting authorization code!", e);
        }

        return url;
    }


    public Credential getAccessToken(String id) {
        LOG.debug("The user id is " + id);
        Credential credential = null;

        try {
            credential = getAuthorizationCodeFlow(id).loadCredential(id);
            LOG.debug("Credential is " + credential);

            if (credential == null) {
                LOG.error("Expected credential but received none!");
            }
        } catch (Exception e) {
            LOG.error("Problem retrieving token!", e);
        }

        return credential;
    }


    public Credential getCredentialFromCode(GoogleAuthResponse auth, String id) {
        Credential credential = null;

        try {
            GoogleAuthorizationCodeFlow acf = getAuthorizationCodeFlow(id);

            AuthorizationCodeTokenRequest req = acf.newTokenRequest(auth.getCode());
            req.setRedirectUri(auth.getRedirectUrl());

            TokenResponse response = req.execute();
            LOG.debug(response.toPrettyString());

            credential = acf.createAndStoreCredential(response, id);
        } catch (Exception e ) {
            LOG.error("Problem creating credential!", e);
        }

        return credential;
    }
}
