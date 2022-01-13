package com.bluntsoftware.shirtshop.controller;

import com.bluntsoftware.shirtshop.model.Customer;
import com.bluntsoftware.shirtshop.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@RestController
@RequestMapping("/rest")
public class CustomerController {

  private final CustomerService service;

  public CustomerController(CustomerService service) {
    this.service = service;
  }

  @PostMapping(value="/customer",produces = MediaType.APPLICATION_JSON_VALUE)
  public Customer save(@RequestBody Map<String,Object> dto){
    ObjectMapper mapper = new ObjectMapper();
    return this.service.save(mapper.convertValue(dto,Customer.class));
  }

  @GetMapping(value = "/customer/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
  public Optional<Customer> findById(@PathVariable("id") String id ){
    return this.service.findById(String.valueOf(id));
  }

  @GetMapping(value = "/customer",produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Customer> findAll(){
    return this.service.findAll();
  }

  @DeleteMapping(value = "/customer/{id}")
  public void deleteById(@PathVariable("id") String id ){
   this.service.deleteById(String.valueOf(id));
  }

  @ResponseBody
  @GetMapping(value = {"/customer/search"}, produces = { "application/json" })
  public Page<Customer> search(@RequestParam(value = "term",  defaultValue = "") String searchTerm,
                             @RequestParam(value = "page",  defaultValue = "0") Integer page,
                             @RequestParam(value = "limit", defaultValue = "50") Integer limit){
    return this.service.search(searchTerm,PageRequest.of(page,limit));
  }
}
