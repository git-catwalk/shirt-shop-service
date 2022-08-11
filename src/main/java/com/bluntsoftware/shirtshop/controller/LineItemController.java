package com.bluntsoftware.shirtshop.controller;

import com.bluntsoftware.shirtshop.model.Invoice;
import com.bluntsoftware.shirtshop.model.LineItem;
import com.bluntsoftware.shirtshop.service.LineItemService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/rest/item")
public class LineItemController {

    private final LineItemService lineItemService;

    public LineItemController(LineItemService lineItemService) {
        this.lineItemService = lineItemService;
    }

    @PostMapping(value="/{orderId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Invoice save(@PathVariable("orderId") String orderId, @RequestBody Map<String,Object> dto){
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return this.lineItemService.save(orderId,mapper.convertValue(dto, LineItem.class));
    }

    @GetMapping("/generate")
    public void regenerateThumbnails(){
        //lineItemService.regenerateThumbnails();
    }

}
