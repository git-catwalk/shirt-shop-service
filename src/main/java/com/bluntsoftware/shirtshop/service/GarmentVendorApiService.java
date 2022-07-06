package com.bluntsoftware.shirtshop.service;

import com.bluntsoftware.shirtshop.model.GarmentVendorCreds;
import com.bluntsoftware.shirtshop.repository.GarmentVendorApiRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class GarmentVendorApiService {
    private static final String VENDOR_ID = "61d5a6c6630a793043b15b8f";
    private final GarmentVendorApiRepo repo;

    public GarmentVendorApiService(GarmentVendorApiRepo repo) {
        this.repo = repo;
    }

    public Optional<GarmentVendorCreds> get() {
        return Optional.of(this.repo.findById(VENDOR_ID)
                .orElse( GarmentVendorCreds.builder().id(VENDOR_ID).build()));
    }

    public GarmentVendorCreds save(GarmentVendorCreds garmentVendor) {
        garmentVendor.setId(VENDOR_ID);
        return this.repo.save(garmentVendor);
    }

}
