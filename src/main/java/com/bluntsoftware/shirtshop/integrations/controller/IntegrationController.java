package com.bluntsoftware.shirtshop.integrations.controller;

import com.bluntsoftware.shirtshop.integrations.service.IntegrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rest/integration")
public class IntegrationController {

    private final IntegrationService integrationService;

    public IntegrationController(IntegrationService integrationService) {
        this.integrationService = integrationService;
    }

    @GetMapping("/{type}/authUrl")
    public ResponseEntity<?> getAuthUrl(@PathVariable("type") String type) {
        String url = integrationService.getAuthUrl(type);
        Map<String,String> ret = new HashMap<>();
        ret.put("url",url);
        return ResponseEntity.ok(ret);
    }

    @PostMapping("/{type}/token")
    public ResponseEntity<?> createToken(@PathVariable("type") String type,@RequestBody Map<String,Object> authCode) {
        Map<String,Object> info = integrationService.createToken(type,authCode);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(info);
    }

    @GetMapping()
    public ResponseEntity<?> list() {
        return ResponseEntity.status(HttpStatus.OK).body(integrationService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteIntegration(@PathVariable("id") String id) {
        integrationService.removeById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }
}
