package com.bluntsoftware.shirtshop.integrations;

import com.bluntsoftware.shirtshop.tenant.TenantResolver;
import org.springframework.stereotype.Service;


@Service
public class IntegrationService {
    private final IntegrationRepo repo;

    public IntegrationService(IntegrationRepo repo) {
        this.repo = repo;
    }

    public Integration get(String id){
        Integration integration =  repo.findById(id).orElse(Integration.builder().build());
        return integration.getTenant() != null && integration.getTenant().equalsIgnoreCase(TenantResolver.resolve()) ?
                integration : null;
    }

    public void save(Integration integration) {
        repo.save(integration);
    }
}
