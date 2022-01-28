package com.bluntsoftware.shirtshop.repository;


import com.bluntsoftware.shirtshop.model.PriceProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceProfileRepo extends MongoRepository<PriceProfile, String> {
}
