package com.bluntsoftware.shirtshop.controller;

import com.bluntsoftware.shirtshop.model.StripeCreds;
import com.bluntsoftware.shirtshop.service.StripeApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/rest/stripe/creds")
public class StripeApiController {
    private final StripeApiService service;

    public StripeApiController(StripeApiService stripeApiService) {
        this.service = stripeApiService;
    }

    @GetMapping(value = "",produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<StripeCreds> get(){
        return this.service.get();
    }

    @PostMapping(value="",produces = MediaType.APPLICATION_JSON_VALUE)
    public StripeCreds save(@RequestBody Map<String,Object> dto){
        ObjectMapper mapper = new ObjectMapper();
        return this.service.save(mapper.convertValue(dto, StripeCreds.class));
    }
}
