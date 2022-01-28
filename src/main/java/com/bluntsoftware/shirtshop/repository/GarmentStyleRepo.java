package com.bluntsoftware.shirtshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.bluntsoftware.shirtshop.model.GarmentStyle;
import org.springframework.stereotype.Repository;

@Repository
public interface GarmentStyleRepo extends MongoRepository<GarmentStyle, String> {
    GarmentStyle findByStyleIdAndReseller(String styleId, String reseller);
    Page<GarmentStyle> findAllByStyleNameIgnoreCaseContainingOrTitleIgnoreCaseContainingOrBrandNameIgnoreCaseContaining(String style, String title, String brand, Pageable pageable);
}
