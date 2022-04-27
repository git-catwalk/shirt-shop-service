package com.bluntsoftware.shirtshop.controller;

import com.bluntsoftware.shirtshop.model.Estimate;
import com.bluntsoftware.shirtshop.model.Invoice;
import com.bluntsoftware.shirtshop.service.EstimateService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@RestController
@RequestMapping("/rest")
public class EstimateController {

  private final EstimateService service;

  public EstimateController(EstimateService service) {
    this.service = service;
  }

  @PostMapping(value="/estimate",produces = MediaType.APPLICATION_JSON_VALUE)
  public Estimate save(@RequestBody Map<String,Object> dto){
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return this.service.save(mapper.convertValue(dto,Estimate.class));
  }

  @GetMapping(value = "/estimate/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
  public Optional<Estimate> findById(@PathVariable("id") String id ){
    return this.service.findById(String.valueOf(id));
  }

  @GetMapping(value = "/estimate",produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Estimate> findAll(){
    return this.service.findAll();
  }

  @DeleteMapping(value = "/estimate/{id}")
  public void deleteById(@PathVariable("id") String id ){
   this.service.deleteById(String.valueOf(id));
  }

  @ResponseBody
  @GetMapping(value = {"/estimate/search"}, produces = { "application/json" })
  public Page<Estimate> search(@RequestParam(value = "term",  defaultValue = "") String searchTerm,
                             @RequestParam(value = "page",  defaultValue = "0") Integer page,
                             @RequestParam(value = "limit", defaultValue = "50") Integer limit){
    return this.service.search(searchTerm,PageRequest.of(page,limit));
  }

  @GetMapping(value = "/estimate/{id}/createInvoice",produces = MediaType.APPLICATION_JSON_VALUE)
  public Invoice createInvoice(@PathVariable("id") String id ){
    return this.service.createInvoice(id);
  }
}
