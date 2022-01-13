package com.bluntsoftware.shirtshop.controller;

import com.bluntsoftware.shirtshop.model.Expenses;
import com.bluntsoftware.shirtshop.service.ExpensesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@RestController
@RequestMapping("/rest")
public class ExpensesController {

  private final ExpensesService service;

  public ExpensesController(ExpensesService service) {
    this.service = service;
  }

  @PostMapping(value="/expenses",produces = MediaType.APPLICATION_JSON_VALUE)
  public Expenses save(@RequestBody Map<String,Object> dto){
    ObjectMapper mapper = new ObjectMapper();
    return this.service.save(mapper.convertValue(dto,Expenses.class));
  }

  @GetMapping(value = "/expenses/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
  public Optional<Expenses> findById(@PathVariable("id") String id ){
    return this.service.findById(String.valueOf(id));
  }

  @GetMapping(value = "/expenses",produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Expenses> findAll(){
    return this.service.findAll();
  }

  @DeleteMapping(value = "/expenses/{id}")
  public void deleteById(@PathVariable("id") String id ){
   this.service.deleteById(String.valueOf(id));
  }

  @ResponseBody
  @GetMapping(value = {"/expenses/search"}, produces = { "application/json" })
  public Page<Expenses> search(@RequestParam(value = "term",  defaultValue = "") String searchTerm,
                             @RequestParam(value = "page",  defaultValue = "0") Integer page,
                             @RequestParam(value = "limit", defaultValue = "50") Integer limit){
    return this.service.search(searchTerm,PageRequest.of(page,limit));
  }
}
