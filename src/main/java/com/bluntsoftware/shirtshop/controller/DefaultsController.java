package com.bluntsoftware.shirtshop.controller;

import com.bluntsoftware.shirtshop.model.Defaults;
import com.bluntsoftware.shirtshop.service.DefaultsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@RestController
@RequestMapping("/rest")
public class DefaultsController {

  private final DefaultsService service;

  public DefaultsController(DefaultsService service) {
    this.service = service;
  }

  @PostMapping(value="/defaults",produces = MediaType.APPLICATION_JSON_VALUE)
  public Defaults save(@RequestBody Map<String,Object> dto){
    ObjectMapper mapper = new ObjectMapper();
    return this.service.save(mapper.convertValue(dto,Defaults.class));
  }

  @GetMapping(value = "/defaults/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
  public Optional<Defaults> findById(@PathVariable("id") String id ){
    return this.service.findById(String.valueOf(id));
  }

  @GetMapping(value = "/defaults",produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Defaults> findAll(){
    return this.service.findAll();
  }

  @DeleteMapping(value = "/defaults/{id}")
  public void deleteById(@PathVariable("id") String id ){
   this.service.deleteById(String.valueOf(id));
  }

  @ResponseBody
  @GetMapping(value = {"/defaults/search"}, produces = { "application/json" })
  public Page<Defaults> search(@RequestParam(value = "term",  defaultValue = "") String searchTerm,
                             @RequestParam(value = "page",  defaultValue = "0") Integer page,
                             @RequestParam(value = "limit", defaultValue = "50") Integer limit){
    return this.service.search(searchTerm,PageRequest.of(page,limit));
  }
}
