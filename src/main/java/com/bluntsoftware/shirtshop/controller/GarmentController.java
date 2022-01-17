package com.bluntsoftware.shirtshop.controller;

import com.bluntsoftware.shirtshop.model.Garment;
import com.bluntsoftware.shirtshop.service.GarmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/rest")
public class GarmentController {

  private final GarmentService service;

  public GarmentController(GarmentService service) {
    this.service = service;
  }

  @PostMapping(value="/garment",produces = MediaType.APPLICATION_JSON_VALUE)
  public Garment save(@RequestBody Map<String,Object> dto){
    ObjectMapper mapper = new ObjectMapper();
    return this.service.save(mapper.convertValue(dto,Garment.class));
  }

  @GetMapping(value = "/garment/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
  public Optional<Garment> findById(@PathVariable("id") String id ){
    return this.service.findById(String.valueOf(id));
  }

  @GetMapping(value = "/garment",produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Garment> findAll(){
    return this.service.findAll();
  }

  @DeleteMapping(value = "/garment/{id}")
  public void deleteById(@PathVariable("id") String id ){
   this.service.deleteById(String.valueOf(id));
  }

  @ResponseBody
  @GetMapping(value = {"/garment/search"}, produces = { "application/json" })
  public Page<Garment> search(@RequestParam(value = "term",  defaultValue = "") String searchTerm,
                             @RequestParam(value = "page",  defaultValue = "0") Integer page,
                             @RequestParam(value = "limit", defaultValue = "50") Integer limit){
    if(searchTerm == null){ searchTerm = "";}
    if(page == null){ page = 0;}
    if(limit == null){ limit = 50;}
    Pageable pageable = PageRequest.of(page,limit);
    return this.service.search(searchTerm,pageable);
  }

  @PostMapping(value = "/garment/import")
  public  Map<String,String> importCsv(@RequestParam("file") MultipartFile file) throws IOException {
    Map<String,String> ret = new HashMap<>();
    this.service.importSSCsv(file.getInputStream());
    ret.put("status","success");
    return ret;
  }
}
