package com.bluntsoftware.shirtshop.controller;

import com.bluntsoftware.shirtshop.model.Customer;
import com.bluntsoftware.shirtshop.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

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
    if(searchTerm == null){ searchTerm = "";}
    if(page == null){ page = 0;}
    if(limit == null){ limit = 50;}
    Pageable pageable = PageRequest.of(page,limit);

    return this.service.search(searchTerm,pageable);
  }

  @PostMapping(value = "/customer/import")
  public  Map<String,String> importCsv(@RequestParam("file") MultipartFile file) throws IOException {
    Map<String,String> ret = new HashMap<>();
    this.service.importCsv(file.getInputStream());
    ret.put("status","success");
    return ret;
  }
}
