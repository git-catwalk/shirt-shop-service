package com.bluntsoftware.shirtshop.service;

import com.bluntsoftware.shirtshop.model.StripeCreds;
import com.bluntsoftware.shirtshop.repository.StripeApiRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class StripeApiService {
    private final StripeApiRepo repo;

    private static final String VENDOR_ID = "61d5a6c6630a793043b15b8c";

    public StripeApiService(StripeApiRepo stripeApiRepo) {
        this.repo = stripeApiRepo;
    }

    public Optional<StripeCreds> get() {
        return Optional.of(this.repo.findById(VENDOR_ID)
                .orElse( StripeCreds.builder().id(VENDOR_ID).build()));
    }

    public StripeCreds save(StripeCreds stripeCreds) {
        stripeCreds.setId(VENDOR_ID);
        return this.repo.save(stripeCreds);
    }

}
