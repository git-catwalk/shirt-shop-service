package com.bluntsoftware.shirtshop.controller;

import com.bluntsoftware.shirtshop.model.Company;
import com.bluntsoftware.shirtshop.service.CompanyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/rest")
public class CompanyController {
    private final CompanyService service;

    public CompanyController(CompanyService companyService) {
        this.service = companyService;
    }

    @GetMapping(value = "/company",produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<Company> get(){
        return this.service.get();
    }

    @PostMapping(value="/company",produces = MediaType.APPLICATION_JSON_VALUE)
    public Company save(@RequestBody Map<String,Object> dto){
        ObjectMapper mapper = new ObjectMapper();
        return this.service.save(mapper.convertValue(dto,Company.class));
    }
}
