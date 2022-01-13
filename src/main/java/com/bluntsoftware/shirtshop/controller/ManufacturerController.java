package com.bluntsoftware.shirtshop.controller;

import com.bluntsoftware.shirtshop.model.Manufacturer;
import com.bluntsoftware.shirtshop.service.ManufacturerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@RestController
@RequestMapping("/rest")
public class ManufacturerController {

  private final ManufacturerService service;

  public ManufacturerController(ManufacturerService service) {
    this.service = service;
  }

  @PostMapping(value="/manufacturer",produces = MediaType.APPLICATION_JSON_VALUE)
  public Manufacturer save(@RequestBody Map<String,Object> dto){
    ObjectMapper mapper = new ObjectMapper();
    return this.service.save(mapper.convertValue(dto,Manufacturer.class));
  }

  @GetMapping(value = "/manufacturer/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
  public Optional<Manufacturer> findById(@PathVariable("id") String id ){
    return this.service.findById(String.valueOf(id));
  }

  @GetMapping(value = "/manufacturer",produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Manufacturer> findAll(){
    return this.service.findAll();
  }

  @DeleteMapping(value = "/manufacturer/{id}")
  public void deleteById(@PathVariable("id") String id ){
   this.service.deleteById(String.valueOf(id));
  }

  @ResponseBody
  @GetMapping(value = {"/manufacturer/search"}, produces = { "application/json" })
  public Page<Manufacturer> search(@RequestParam(value = "term",  defaultValue = "") String searchTerm,
                             @RequestParam(value = "page",  defaultValue = "0") Integer page,
                             @RequestParam(value = "limit", defaultValue = "50") Integer limit){
    return this.service.search(searchTerm,PageRequest.of(page,limit));
  }
}
