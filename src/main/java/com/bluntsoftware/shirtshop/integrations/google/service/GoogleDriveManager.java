package com.bluntsoftware.shirtshop.integrations.google.service;

import com.bluntsoftware.shirtshop.integrations.google.model.Constants;
import com.bluntsoftware.shirtshop.integrations.google.util.GoogleOauthUtil;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.security.GeneralSecurityException;
import static com.bluntsoftware.shirtshop.integrations.google.model.Constants.GOOGLE_DRIVE_ID;

@Service
public class GoogleDriveManager {
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final GoogleOauthUtil googleOauthUtil;

    public GoogleDriveManager( GoogleOauthUtil googleOauthUtil) {
        this.googleOauthUtil = googleOauthUtil;
    }

    public Drive getInstance() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, googleOauthUtil.getCredential( GOOGLE_DRIVE_ID))
                .setApplicationName(Constants.APPLICATION_NAME)
                .build();
    }
}
