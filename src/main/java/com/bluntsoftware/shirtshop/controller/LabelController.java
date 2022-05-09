package com.bluntsoftware.shirtshop.controller;

import com.bluntsoftware.shirtshop.model.Label;
import com.bluntsoftware.shirtshop.service.LabelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController("/rest/label")
public class LabelController {

    private final LabelService service;

    public LabelController(LabelService service) {
        this.service = service;
    }


    @PostMapping(value="",produces = MediaType.APPLICATION_JSON_VALUE)
    public Label save(@RequestBody Map<String,Object> dto){
        ObjectMapper mapper = new ObjectMapper();
        return this.service.save(mapper.convertValue(dto, Label.class));
    }

    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<Label> findById(@PathVariable("id") String id ){
        return this.service.findById(String.valueOf(id));
    }

    @GetMapping(value = "",produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Label> findAll(){
        return this.service.findAll();
    }

    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable("id") String id ){
        this.service.deleteById(String.valueOf(id));
    }

    @ResponseBody
    @GetMapping(value = {"/search"}, produces = { "application/json" })
    public Page<Label> search(@RequestParam(value = "term",  defaultValue = "") String searchTerm,
                                 @RequestParam(value = "page",  defaultValue = "0") Integer page,
                                 @RequestParam(value = "limit", defaultValue = "50") Integer limit,
                                 @RequestParam(value = "sord",required = false,defaultValue = "ASC") String sord,
                                 @RequestParam(value = "sort",required = false) String sort){
        if(searchTerm == null){ searchTerm = "";}
        if(page == null){ page = 0;}
        if(limit == null){ limit = 50;}
        Sort sorter = StringUtils.isEmpty(sort) ? Sort.unsorted() : Sort.by(Sort.Direction.fromString(sord),sort);
        Pageable pageable = PageRequest.of(page,limit,sorter);
        return this.service.search(searchTerm,pageable);
    }
}
