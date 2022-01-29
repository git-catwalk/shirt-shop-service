package com.bluntsoftware.shirtshop.repository;


import com.bluntsoftware.shirtshop.model.GarmentVendorApi;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GarmentVendorApiRepo extends MongoRepository<GarmentVendorApi, String> {

}
