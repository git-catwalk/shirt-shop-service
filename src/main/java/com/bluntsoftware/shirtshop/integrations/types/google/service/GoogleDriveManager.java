package com.bluntsoftware.shirtshop.integrations.types.google.service;

import com.bluntsoftware.shirtshop.integrations.service.IntegrationService;
import com.bluntsoftware.shirtshop.integrations.types.google.config.GoogleConfig;
import com.bluntsoftware.shirtshop.integrations.types.google.oauth.GDriveOAuthIntegration;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;


@Service
public class GoogleDriveManager {
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final IntegrationService integrationService;
    private final ObjectMapper objectMapper;
    private final GoogleConfig googleConfig;

    public GoogleDriveManager(IntegrationService integrationService, ObjectMapper objectMapper, GoogleConfig googleConfig) {
        this.integrationService = integrationService;
        this.objectMapper = objectMapper;
        this.googleConfig = googleConfig;
    }

    public Drive getInstance() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredential())
                .setApplicationName("Shirt-Shop")
                .build();
    }

    private Credential getCredential() throws GeneralSecurityException, IOException {
        Map<String,Object> credentials = this.integrationService.getCredentials(GDriveOAuthIntegration.GOOGLE_DRIVE_ID);
        objectMapper.configure(DeserializationFeature.USE_LONG_FOR_INTS, true);
        TokenResponse tokenResponse = objectMapper.convertValue(credentials,TokenResponse.class);
        Credential credential = new Credential.Builder(BearerToken.authorizationHeaderAccessMethod())
                .setJsonFactory(JSON_FACTORY)
                .setTransport(GoogleNetHttpTransport.newTrustedTransport())
                .setTokenServerEncodedUrl(googleConfig.getTokenUrl())
                .setClientAuthentication(new ClientParametersAuthentication(googleConfig.getClientId(), googleConfig.getClientSecret()))
                .build();
        return credential.setFromTokenResponse(tokenResponse);
    }

}
