package com.bluntsoftware.shirtshop.controller;

import com.bluntsoftware.shirtshop.model.Invoice;
import com.bluntsoftware.shirtshop.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.StripeException;
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
public class OrderController {
    private final OrderService service;
    private final ObjectMapper mapper;
    public OrderController(OrderService service, ObjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping(value = "/finalize",produces = MediaType.APPLICATION_JSON_VALUE)
    public Invoice finalizeInvoice(@RequestBody Map<String,Object> dto) throws StripeException {
        return this.service.finalizeInvoice(mapper.convertValue(dto, Invoice.class));
    }

    @PostMapping(value="",produces = MediaType.APPLICATION_JSON_VALUE)
    public Invoice save(@RequestBody Map<String,Object> dto){
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
