package com.bluntsoftware.shirtshop.service;

import com.bluntsoftware.shirtshop.model.Company;
import com.bluntsoftware.shirtshop.repository.CompanyRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class CompanyService {
    private static final String COMPANY_ID = "61e5a6e6630d793043b15b8f";
    private final CompanyRepo repo;

    public CompanyService(CompanyRepo repo) {
        this.repo = repo;
    }

    public Optional<Company> get() {
        return Optional.of(this.repo.findById(COMPANY_ID)
                .orElse( Company.builder().id(COMPANY_ID).build()));
    }

    public Company save(Company company) {
        company.setId(COMPANY_ID);
        return this.repo.save(company);
    }
}
