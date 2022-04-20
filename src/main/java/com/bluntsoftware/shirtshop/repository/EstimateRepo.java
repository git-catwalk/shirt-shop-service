package com.bluntsoftware.shirtshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.bluntsoftware.shirtshop.model.Estimate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstimateRepo extends MongoRepository<Estimate, String> {

    Page<Estimate> findAllByCustomer_NameIgnoreCaseContaining(String term, Pageable pageable);
}
