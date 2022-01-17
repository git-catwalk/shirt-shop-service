package com.bluntsoftware.shirtshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.bluntsoftware.shirtshop.model.Garment;
import org.springframework.stereotype.Repository;

@Repository
public interface GarmentRepo extends MongoRepository<Garment, String> {
    Page<Garment> findAllByBrandIgnoreCaseContaining(String term, Pageable pageable);
    Page<Garment> findAllByStyleNumberIgnoreCaseContainingOrBrandIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(String style,String brand,String description,Pageable pageable);
}
