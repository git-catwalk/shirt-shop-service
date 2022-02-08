package com.bluntsoftware.shirtshop.google.controller;

import com.bluntsoftware.shirtshop.google.util.GoogleOauthUtil;
import com.bluntsoftware.shirtshop.google.model.GoogleAuthResponse;
import com.bluntsoftware.shirtshop.model.Company;
import com.bluntsoftware.shirtshop.service.CompanyService;
import com.google.api.client.auth.oauth2.Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/rest/drive")
public class GDriveController {

    private static final Logger LOG = LoggerFactory.getLogger(GDriveController.class);
    private final GoogleOauthUtil googleOauthUtil;
    private final CompanyService companyService;

    public GDriveController(GoogleOauthUtil googleOauthUtil, CompanyService companyService) {
        this.googleOauthUtil = googleOauthUtil;
        this.companyService = companyService;
    }

    @GetMapping("/authorizationCode")
    public ResponseEntity<?> getAuthorizationCodeUrl(@RequestParam("redirectUrl") String redirectUrl) {
        Company currentUser = companyService.get().get();
        String url = googleOauthUtil.getAuthorizationCodeUrl(currentUser.getId(), redirectUrl);
        Map<String,String> ret = new HashMap<>();
        ret.put("url",url);
        return ResponseEntity.ok(ret);
    }

    @PostMapping("/token")
    public ResponseEntity<?> createToken(@RequestBody GoogleAuthResponse auth) {
        LOG.debug("In createToken " + auth);
        Company currentUser = companyService.get().get();
        try {
            Credential credential = googleOauthUtil.getCredentialFromCode(auth, currentUser.getId());
            return ResponseEntity.ok(credential.getAccessToken());
        } catch (Exception e) {
            LOG.error("Problem retrieving credential!", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem creating token!");
    }

}
