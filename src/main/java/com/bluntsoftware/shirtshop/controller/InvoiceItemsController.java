package com.bluntsoftware.shirtshop.controller;

import com.bluntsoftware.shirtshop.model.InvoiceItems;
import com.bluntsoftware.shirtshop.service.InvoiceItemsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@RestController
@RequestMapping("/rest")
public class InvoiceItemsController {

  private final InvoiceItemsService service;

  public InvoiceItemsController(InvoiceItemsService service) {
    this.service = service;
  }

  @PostMapping(value="/invoiceItems",produces = MediaType.APPLICATION_JSON_VALUE)
  public InvoiceItems save(@RequestBody Map<String,Object> dto){
    ObjectMapper mapper = new ObjectMapper();
    return this.service.save(mapper.convertValue(dto,InvoiceItems.class));
  }

  @GetMapping(value = "/invoiceItems/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
  public Optional<InvoiceItems> findById(@PathVariable("id") String id ){
    return this.service.findById(String.valueOf(id));
  }

  @GetMapping(value = "/invoiceItems",produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<InvoiceItems> findAll(){
    return this.service.findAll();
  }

  @DeleteMapping(value = "/invoiceItems/{id}")
  public void deleteById(@PathVariable("id") String id ){
   this.service.deleteById(String.valueOf(id));
  }

  @ResponseBody
  @GetMapping(value = {"/invoiceItems/search"}, produces = { "application/json" })
  public Page<InvoiceItems> search(@RequestParam(value = "term",  defaultValue = "") String searchTerm,
                             @RequestParam(value = "page",  defaultValue = "0") Integer page,
                             @RequestParam(value = "limit", defaultValue = "50") Integer limit){
    return this.service.search(searchTerm,PageRequest.of(page,limit));
  }
}
