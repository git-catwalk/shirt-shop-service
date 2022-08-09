package com.bluntsoftware.shirtshop.integrations.service;

import com.bluntsoftware.shirtshop.integrations.model.Integration;
import com.bluntsoftware.shirtshop.integrations.repository.IntegrationRepo;
import com.bluntsoftware.shirtshop.integrations.types.OAuthIntegration;
import com.bluntsoftware.shirtshop.integrations.types.OAuthIntegrationFactory;
import com.bluntsoftware.shirtshop.tenant.TenantResolver;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IntegrationService {
    private final IntegrationRepo integrationRepo;
    private final OAuthIntegrationFactory integrationFactory;

    public IntegrationService(IntegrationRepo integrationRepo, OAuthIntegrationFactory integrationFactory) {
        this.integrationRepo = integrationRepo;
        this.integrationFactory = integrationFactory;
    }

    public Integration get(String id){
        return integrationRepo.findById(id).orElse(Integration.builder().build());
    }

    public void save(Integration integration) {
        integrationRepo.save(integration);
    }

    public String getAuthUrl(String type) {
        OAuthIntegration oAuthIntegration = integrationFactory.createIntegration(type);
        return oAuthIntegration.getAuthUrl();
    }

    public Integration createToken(String type, Map<String, Object> authCode) {
        OAuthIntegration oAuthIntegration = integrationFactory.createIntegration(type);
        Map<String, Object> credentials =  oAuthIntegration.getCredentials(authCode);
        return saveIntegration(oAuthIntegration,credentials);
    }

    public boolean hasCredentials(String type) {
        return get(type) != null;
    }

    public List<Integration> findAll() {
        return integrationRepo.findAll();
    }

    public Map<String,Object> getCredentials(String type){
        Integration integration = get(type);
        if(integration != null && isExpired(integration)){
            OAuthIntegration oAuthIntegration = integrationFactory.createIntegration(type);
            return saveIntegration(oAuthIntegration,
                    oAuthIntegration.refreshCredentials(integration.getCredentials())).getCredentials();
        }
        return integration != null? integration.getCredentials() : new HashMap<>();
    }

    private Integration saveIntegration(OAuthIntegration oAuthIntegration,Map<String, Object> credentials){
        return this.integrationRepo.save(Integration.builder()
                .id(oAuthIntegration.getType())
                .credentials(credentials)
                .issued(new Date())
                .expires(oAuthIntegration.getExpiresAt(credentials))
                .tenant(TenantResolver.resolve())
                .build());
    }

    private boolean isExpired(Integration integration) {
        return integration.getExpires().compareTo(new Date()) < 0;
    }

    public void removeById(String id) {
        integrationRepo.deleteById(id);
    }

    public Map<String,Object> getStatus(String type) {
        Map<String,Object> ret = new HashMap<>();
        Integration integration = get(type);
        ret.put("type",type);
        ret.put("configured",integration.getCredentials() != null);
        return  ret;
    }
}
