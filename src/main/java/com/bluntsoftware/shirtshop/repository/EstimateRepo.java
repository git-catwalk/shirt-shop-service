package com.bluntsoftware.shirtshop.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.bluntsoftware.shirtshop.model.Estimate;
import org.springframework.stereotype.Repository;

@Repository
public interface EstimateRepo extends MongoRepository<Estimate, String> {
}