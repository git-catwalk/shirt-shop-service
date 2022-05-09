package com.bluntsoftware.shirtshop.repository;

import com.bluntsoftware.shirtshop.model.Label;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelRepo  extends MongoRepository<Label, String> {
    Page<Label> findAllByNameIgnoreCaseContaining(String term, Pageable pageable);
}
