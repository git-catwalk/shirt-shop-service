package com.bluntsoftware.shirtshop.controller;

import com.bluntsoftware.shirtshop.model.AuditTrail;
import com.bluntsoftware.shirtshop.service.AuditTrailService;
import com.bluntsoftware.shirtshop.service.InvoiceService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rest/audit")
public class AuditTrailController {
    private final AuditTrailService service;
    private final InvoiceService orderService;

    public AuditTrailController(AuditTrailService service, InvoiceService invoiceService) {
        this.service = service;
        this.orderService = invoiceService;
    }

    @GetMapping(value = "/estimate/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    List<AuditTrail> auditEstimate(@PathVariable("id") String id){
        List<String> where = new ArrayList<>();
        where.add(id);
        return this.service.findAllByWhere(where);
    }

    @GetMapping(value = "/order/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    List<AuditTrail> auditOrder(@PathVariable("id") String id){
        List<String> where = new ArrayList<>();
        orderService.findById(id).ifPresent(o->{
            where.add(id);
            if(o.getEstimateId() != null){
                where.add(o.getEstimateId());
            }
            o.getItems().forEach(i->{
                if(i.getId() != null){
                    where.add(id + "-" + i.getId());
                }
            });
        });
        return this.service.findAllByWhere(where);
    }

    @GetMapping(value = "/order/{orderId}/lineItem/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    List<AuditTrail> auditLineItem(@PathVariable("orderId") String orderId,@PathVariable("id") String id){
        List<String> where = new ArrayList<>();
        where.add(orderId + "-" + id);
        return this.service.findAllByWhere(where);
    }
}
