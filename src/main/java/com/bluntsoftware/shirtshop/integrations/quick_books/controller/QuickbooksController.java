package com.bluntsoftware.shirtshop.integrations.quick_books.controller;

import com.bluntsoftware.shirtshop.integrations.AuthResponse;
import com.bluntsoftware.shirtshop.integrations.quick_books.service.QuickbooksService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/rest/qb")
public class QuickbooksController {

    private final QuickbooksService quickbooksService;

    public QuickbooksController(QuickbooksService quickbooksService) {
        this.quickbooksService = quickbooksService;
    }

    @GetMapping(value = "/authorizationCode",produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,String> authorize(){
        return this.quickbooksService.authUrl();
    }

    @PostMapping("/token")
    public ResponseEntity<?> createToken(@RequestBody AuthResponse auth) {
        return this.quickbooksService.token(auth);
    }

}
