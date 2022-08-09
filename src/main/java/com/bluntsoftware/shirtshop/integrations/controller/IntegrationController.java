package com.bluntsoftware.shirtshop.integrations.controller;

import com.bluntsoftware.shirtshop.integrations.model.Integration;
import com.bluntsoftware.shirtshop.integrations.model.IntegrationDto;
import com.bluntsoftware.shirtshop.integrations.service.IntegrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/integration")
public class IntegrationController {

    private final IntegrationService integrationService;

    public IntegrationController(IntegrationService integrationService) {
        this.integrationService = integrationService;
    }

    @GetMapping("/{type}/authUrl")
    public ResponseEntity<Map<String,String>> getAuthUrl(@PathVariable("type") String type) {
        String url = integrationService.getAuthUrl(type);
        Map<String,String> ret = new HashMap<>();
        ret.put("url",url);
        return ResponseEntity.ok(ret);
    }

    @GetMapping("/{type}/status")
    public ResponseEntity<Map<String,Object>> getStatus(@PathVariable("type") String type) {
        return ResponseEntity.ok(integrationService.getStatus(type));
    }
    
    @PostMapping("/{type}/token")
    public ResponseEntity<IntegrationDto> createToken(@PathVariable("type") String type,@RequestBody Map<String,Object> authCode) {
        IntegrationDto dto = mapIntegrationToIntegrationDto(integrationService.createToken(type,authCode));
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @GetMapping()
    public ResponseEntity<List<IntegrationDto>> list() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(integrationService.findAll()
                        .stream()
                        .map(this::mapIntegrationToIntegrationDto)
                        .collect(Collectors.toList()));
    }

    IntegrationDto mapIntegrationToIntegrationDto(Integration integration){
        return IntegrationDto.builder()
                .id(integration.getId())
                .expires(integration.getExpires())
                .issued(integration.getIssued())
                .tenant(integration.getTenant())
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteIntegration(@PathVariable("id") String id) {
        integrationService.removeById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }
}
