package com.bluntsoftware.shirtshop.controller;

import com.bluntsoftware.shirtshop.model.Invoice;
import com.bluntsoftware.shirtshop.service.InvoiceService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
@RestController
@RequestMapping("/rest/invoice")
public class InvoiceController {
    private final InvoiceService service;

    public InvoiceController(InvoiceService service) {
        this.service = service;
    }

    @PostMapping(value="",produces = MediaType.APPLICATION_JSON_VALUE)
    public Invoice save(@RequestBody Map<String,Object> dto){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return this.service.save(mapper.convertValue(dto, Invoice.class));
    }

    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<Invoice> findById(@PathVariable("id") String id ){
        return this.service.findById(String.valueOf(id));
    }

    @GetMapping(value = "",produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Invoice> findAll(){
        return this.service.findAll();
    }

    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable("id") String id ){
        this.service.deleteById(String.valueOf(id));
    }

    @ResponseBody
    @GetMapping(value = {"/search"}, produces = { "application/json" })
    public Page<Invoice> search(@RequestParam(value = "term",  defaultValue = "") String searchTerm,
                                 @RequestParam(value = "page",  defaultValue = "0") Integer page,
                                 @RequestParam(value = "limit", defaultValue = "50") Integer limit,
                                @RequestParam(value = "sord",required = false,defaultValue = "ASC") String sord,
                                @RequestParam(value = "sort",required = false) String sort){
        Sort sorter = StringUtils.isEmpty(sort) ? Sort.unsorted() : Sort.by(Sort.Direction.fromString(sord),sort);
        return this.service.search(searchTerm, PageRequest.of(page,limit,sorter));
    }

}
