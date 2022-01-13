package com.bluntsoftware.shirtshop.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.bluntsoftware.shirtshop.model.Customer;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends MongoRepository<Customer, String> {
}