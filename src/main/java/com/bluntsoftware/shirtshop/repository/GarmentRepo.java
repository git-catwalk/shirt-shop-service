package com.bluntsoftware.shirtshop.repository;

import com.bluntsoftware.shirtshop.model.Garment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface GarmentRepo extends MongoRepository<Garment, String> {
    List<Garment> findAllByStyleId(String styleId);
}
