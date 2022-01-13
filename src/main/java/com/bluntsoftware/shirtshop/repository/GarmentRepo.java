package com.bluntsoftware.shirtshop.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.bluntsoftware.shirtshop.model.Garment;
import org.springframework.stereotype.Repository;

@Repository
public interface GarmentRepo extends MongoRepository<Garment, String> {
}