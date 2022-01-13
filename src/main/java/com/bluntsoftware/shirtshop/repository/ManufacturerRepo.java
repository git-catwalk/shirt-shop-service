package com.bluntsoftware.shirtshop.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.bluntsoftware.shirtshop.model.Manufacturer;
import org.springframework.stereotype.Repository;

@Repository
public interface ManufacturerRepo extends MongoRepository<Manufacturer, String> {
}