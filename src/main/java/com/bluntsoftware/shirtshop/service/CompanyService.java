package com.bluntsoftware.shirtshop.service;

import com.bluntsoftware.shirtshop.model.Company;
import com.bluntsoftware.shirtshop.repository.CompanyRepo;
import com.bluntsoftware.shirtshop.tenant.TenantResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class CompanyService {

    private final CompanyRepo repo;

    public CompanyService(CompanyRepo repo) {
        this.repo = repo;
    }

    public Optional<Company> get() {
        return Optional.of(this.repo.findById(TenantResolver.resolve())
                .orElse( Company.builder().id(TenantResolver.resolve()).build()));
    }

    public Company save(Company company) {
        company.setId(TenantResolver.resolve());
        return this.repo.save(company);
    }
}
