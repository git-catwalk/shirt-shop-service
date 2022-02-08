package com.bluntsoftware.shirtshop.google.service;

import com.bluntsoftware.shirtshop.google.model.Constants;
import com.bluntsoftware.shirtshop.google.util.GoogleOauthUtil;
import com.bluntsoftware.shirtshop.model.Company;
import com.bluntsoftware.shirtshop.service.CompanyService;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
public class GoogleDriveManager {
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private final CompanyService companyService;
    private final GoogleOauthUtil googleOauthUtil;
    public GoogleDriveManager(CompanyService companyService, GoogleOauthUtil googleOauthUtil) {
        this.companyService = companyService;
        this.googleOauthUtil = googleOauthUtil;
    }

    public Drive getInstance() throws GeneralSecurityException, IOException {
        Company company = this.companyService.get().get();


        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, googleOauthUtil.getCredential( company.getId()))
                .setApplicationName(Constants.APPLICATION_NAME)
                .build();
    }
}
