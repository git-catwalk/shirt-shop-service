package com.bluntsoftware.shirtshop.controller;

import com.bluntsoftware.shirtshop.model.GarmentVendorCreds;
import com.bluntsoftware.shirtshop.service.GarmentVendorApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/rest/garment/vendor")
public class GarmentVendorApiController {

    private final GarmentVendorApiService service;

    public GarmentVendorApiController(GarmentVendorApiService garmentVendorService) {
        this.service = garmentVendorService;
    }

    @GetMapping(value = "",produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<GarmentVendorCreds> get(){
        return this.service.get();
    }

    @PostMapping(value="",produces = MediaType.APPLICATION_JSON_VALUE)
    public GarmentVendorCreds save(@RequestBody Map<String,Object> dto){
        ObjectMapper mapper = new ObjectMapper();
        return this.service.save(mapper.convertValue(dto, GarmentVendorCreds.class));
    }

}
