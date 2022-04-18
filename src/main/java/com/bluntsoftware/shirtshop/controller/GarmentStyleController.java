package com.bluntsoftware.shirtshop.controller;

import com.bluntsoftware.shirtshop.model.Garment;
import com.bluntsoftware.shirtshop.model.GarmentStyle;
import com.bluntsoftware.shirtshop.model.GarmentColor;

import com.bluntsoftware.shirtshop.service.GarmentStyleService;
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
@RequestMapping("/rest/garment/style")
public class GarmentStyleController {

  private final GarmentStyleService service;

  public GarmentStyleController(GarmentStyleService service) {
    this.service = service;
  }

  @PostMapping(value="",produces = MediaType.APPLICATION_JSON_VALUE)
  public GarmentStyle save(@RequestBody Map<String,Object> dto){
    ObjectMapper mapper = new ObjectMapper();
    return this.service.save(mapper.convertValue(dto, GarmentStyle.class));
  }

  @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
  public Optional<GarmentStyle> findById(@PathVariable("id") String id ){
    return this.service.findById(String.valueOf(id));
  }

  @GetMapping(value = "",produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<GarmentStyle> findAll(){
    return this.service.findAll();
  }

  @DeleteMapping(value = "/{id}")
  public void deleteById(@PathVariable("id") String id ){
   this.service.deleteById(String.valueOf(id));
  }

  @DeleteMapping(value = "")
  public void deleteAll(){
    this.service.deleteAll();
  }

  @ResponseBody
  @GetMapping(value = {"/search"}, produces = { "application/json" })
  public Page<GarmentStyle> search(@RequestParam(value = "term",  defaultValue = "") String searchTerm,
                                   @RequestParam(value = "page",  defaultValue = "0") Integer page,
                                   @RequestParam(value = "limit", defaultValue = "50") Integer limit){
    if(searchTerm == null){ searchTerm = "";}
    if(page == null){ page = 0;}
    if(limit == null){ limit = 50;}
    Pageable pageable = PageRequest.of(page,limit);
    return this.service.search(searchTerm,pageable);
  }

  @PostMapping(value = "/import")
  public  Map<String,String> importCsv(@RequestParam("file") MultipartFile file) throws IOException {
    Map<String,String> ret = new HashMap<>();
    this.service.importSSCsv(file.getInputStream());
    ret.put("status","success");
    return ret;
  }

  @GetMapping(value = "/{styleId}/colors",produces = MediaType.APPLICATION_JSON_VALUE)
  public List<GarmentColor> listColors(@PathVariable("styleId") String styleId){
    return this.service.findColors(styleId);
  }

  @GetMapping(value = "/{styleId}/{colorId}/sizes",produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Garment> listGarments(@PathVariable("styleId") String styleId, @PathVariable("colorId") String colorId){
    return this.service.findGarments(styleId,colorId);
  }

}
