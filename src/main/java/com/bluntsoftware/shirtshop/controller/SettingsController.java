package com.bluntsoftware.shirtshop.controller;

import com.bluntsoftware.shirtshop.model.Settings;
import com.bluntsoftware.shirtshop.service.SettingsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@RestController
@RequestMapping("/rest")
public class SettingsController {

  private final SettingsService service;

  public SettingsController(SettingsService service) {
    this.service = service;
  }

  @PostMapping(value="/settings",produces = MediaType.APPLICATION_JSON_VALUE)
  public Settings save(@RequestBody Map<String,Object> dto){
    ObjectMapper mapper = new ObjectMapper();
    return this.service.save(mapper.convertValue(dto,Settings.class));
  }

  @GetMapping(value = "/settings/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
  public Optional<Settings> findById(@PathVariable("id") String id ){
    return this.service.findById(String.valueOf(id));
  }

  @GetMapping(value = "/settings",produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Settings> findAll(){
    return this.service.findAll();
  }

  @DeleteMapping(value = "/settings/{id}")
  public void deleteById(@PathVariable("id") String id ){
   this.service.deleteById(String.valueOf(id));
  }

  @ResponseBody
  @GetMapping(value = {"/settings/search"}, produces = { "application/json" })
  public Page<Settings> search(@RequestParam(value = "term",  defaultValue = "") String searchTerm,
                             @RequestParam(value = "page",  defaultValue = "0") Integer page,
                             @RequestParam(value = "limit", defaultValue = "50") Integer limit){
    return this.service.search(searchTerm,PageRequest.of(page,limit));
  }
}
