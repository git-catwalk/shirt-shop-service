package com.bluntsoftware.shirtshop.integrations;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntegrationRepo extends MongoRepository<Integration, String> {


}
