package com.bluntsoftware.shirtshop.integrations.types.quick_books.service;

import com.bluntsoftware.shirtshop.integrations.service.IntegrationService;
import com.bluntsoftware.shirtshop.integrations.types.quick_books.config.QuickbooksApiConfig;
import com.intuit.ipp.core.Context;
import com.intuit.ipp.core.ServiceType;
import com.intuit.ipp.exception.FMSException;
import com.intuit.ipp.security.OAuth2Authorizer;
import com.intuit.ipp.services.DataService;
import com.intuit.ipp.util.Config;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.bluntsoftware.shirtshop.integrations.types.quick_books.oauth.QuickBooksOAuthIntegration.QUICK_BOOKS_ID;

@Service
public class QuickBooksManager {
    private final IntegrationService integrationService;
    private final QuickbooksApiConfig qbApiConfig;
    public QuickBooksManager(IntegrationService integrationService, QuickbooksApiConfig qbApiConfig) {
        this.integrationService = integrationService;
        this.qbApiConfig = qbApiConfig;
    }

    public DataService getDataService()   {
        Config.setProperty(Config.BASE_URL_QBO,  qbApiConfig.getUrl() + "/v3/company");
        Map<String,Object> credentials = this.integrationService.getCredentials( QUICK_BOOKS_ID);
        String accessToken = credentials.get("access_token").toString();
        String realmId = credentials.get("realm_id").toString();
        OAuth2Authorizer authorizer = new OAuth2Authorizer(accessToken);
        Context context = null;
        try {
            context = new Context(authorizer, ServiceType.QBO, realmId);
        } catch (FMSException e) {
            e.printStackTrace();
        }
        return new DataService(context);
    }

}
