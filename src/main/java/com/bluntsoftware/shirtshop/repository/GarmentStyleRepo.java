package com.bluntsoftware.shirtshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.bluntsoftware.shirtshop.model.GarmentStyle;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GarmentStyleRepo extends MongoRepository<GarmentStyle, String> {
    Page<GarmentStyle> findAllBy(TextCriteria criteria, Pageable pageable);
    GarmentStyle findByStyleIdAndReseller(String styleId, String reseller);
    Page<GarmentStyle> findAllByStyleNameIgnoreCaseContainingOrTitleIgnoreCaseContainingOrBrandNameIgnoreCaseContaining(String style, String title, String brand, Pageable pageable);
    List<GarmentStyle> findAllByBrandNameIgnoreCaseContainingAndTitleIgnoreCaseContaining(String brand, String title);

}
