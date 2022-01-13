package com.bluntsoftware.shirtshop.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.bluntsoftware.shirtshop.model.Settings;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsRepo extends MongoRepository<Settings, String> {
}