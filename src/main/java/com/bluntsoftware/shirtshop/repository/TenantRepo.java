package com.bluntsoftware.shirtshop.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.bluntsoftware.shirtshop.model.Tenant;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantRepo extends MongoRepository<Tenant, String> {
}