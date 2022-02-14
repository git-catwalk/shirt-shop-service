package com.bluntsoftware.shirtshop.integrations;


import org.springframework.stereotype.Service;


@Service
public class IntegrationService {
    private final IntegrationRepo repo;

    public IntegrationService(IntegrationRepo repo) {
        this.repo = repo;
    }

    public Integration get(String id){
        return repo.findById(id).orElse(Integration.builder().build());
    }

    public void save(Integration integration) {
        repo.save(integration);
    }
}
