package com.bluntsoftware.shirtshop.integrations.repository;

import com.bluntsoftware.shirtshop.integrations.model.Integration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntegrationRepo extends MongoRepository<Integration, String> {
}
