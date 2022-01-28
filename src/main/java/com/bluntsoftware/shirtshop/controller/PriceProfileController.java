package com.bluntsoftware.shirtshop.controller;

import com.bluntsoftware.shirtshop.model.PriceProfile;
import com.bluntsoftware.shirtshop.service.PriceProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
@RestController
@RequestMapping("/rest")
public class PriceProfileController {
    private final PriceProfileService service;

    public PriceProfileController(PriceProfileService service) {
        this.service = service;
    }

    @PostMapping(value="/price-profile",produces = MediaType.APPLICATION_JSON_VALUE)
    public PriceProfile save(@RequestBody Map<String,Object> dto){
        ObjectMapper mapper = new ObjectMapper();
        return this.service.save(mapper.convertValue(dto,PriceProfile.class));
    }

    @GetMapping(value = "/price-profile/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<PriceProfile> findById(@PathVariable("id") String id ){
        return this.service.findById(String.valueOf(id));
    }

    @GetMapping(value = "/price-profile",produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<PriceProfile> findAll(){
        return this.service.findAll();
    }

    @DeleteMapping(value = "/price-profile/{id}")
    public void deleteById(@PathVariable("id") String id ){
        this.service.deleteById(String.valueOf(id));
    }

    @ResponseBody
    @GetMapping(value = {"/price-profile/search"}, produces = { "application/json" })
    public Page<PriceProfile> search(@RequestParam(value = "term",  defaultValue = "") String searchTerm,
                                 @RequestParam(value = "page",  defaultValue = "0") Integer page,
                                 @RequestParam(value = "limit", defaultValue = "50") Integer limit){
        return this.service.search(searchTerm, PageRequest.of(page,limit));
    }
}
