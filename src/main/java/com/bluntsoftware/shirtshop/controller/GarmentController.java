package com.bluntsoftware.shirtshop.controller;


import com.bluntsoftware.shirtshop.model.Garment;
import com.bluntsoftware.shirtshop.service.GarmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
@RestController
@RequestMapping("/rest/garment")
public class GarmentController {

    private final GarmentService service;

    public GarmentController(GarmentService service) {
        this.service = service;
    }

    @PostMapping(value="",produces = MediaType.APPLICATION_JSON_VALUE)
    public Garment save(@RequestBody Map<String,Object> dto){
        ObjectMapper mapper = new ObjectMapper();
        return this.service.save(mapper.convertValue(dto, Garment.class));
    }

    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<Garment> findById(@PathVariable("id") String id ){
        return this.service.findById(String.valueOf(id));
    }

    @GetMapping(value = "",produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Garment> findAll(){
        return this.service.findAll();
    }

    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable("id") String id ){
        this.service.deleteById(String.valueOf(id));
    }

    @ResponseBody
    @GetMapping(value = {"/search"}, produces = { "application/json" })
    public Page<Garment> search(@RequestParam(value = "term",  defaultValue = "") String searchTerm,
                                     @RequestParam(value = "page",  defaultValue = "0") Integer page,
                                     @RequestParam(value = "limit", defaultValue = "50") Integer limit){
        return this.service.search(searchTerm, PageRequest.of(page,limit));
    }
}
