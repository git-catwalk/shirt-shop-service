package com.bluntsoftware.shirtshop.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.bluntsoftware.shirtshop.model.Defaults;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultsRepo extends MongoRepository<Defaults, String> {
}