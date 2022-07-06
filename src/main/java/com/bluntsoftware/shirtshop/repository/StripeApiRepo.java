package com.bluntsoftware.shirtshop.repository;


import com.bluntsoftware.shirtshop.model.StripeCreds;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StripeApiRepo  extends MongoRepository<StripeCreds, String> {
}
