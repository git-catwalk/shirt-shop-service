package com.bluntsoftware.shirtshop.repository;

import com.bluntsoftware.shirtshop.model.Company;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepo extends MongoRepository<Company, String> {
}
