package com.bluntsoftware.shirtshop.integrations.google.controller;

import com.bluntsoftware.shirtshop.integrations.Integration;
import com.bluntsoftware.shirtshop.integrations.IntegrationService;
import com.bluntsoftware.shirtshop.integrations.google.util.GoogleOauthUtil;
import com.bluntsoftware.shirtshop.integrations.google.model.GoogleAuthResponse;
import com.google.api.client.auth.oauth2.Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

import static com.bluntsoftware.shirtshop.integrations.google.model.Constants.GOOGLE_DRIVE_ID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/rest/drive")
public class GDriveController {

    private static final Logger LOG = LoggerFactory.getLogger(GDriveController.class);
    private final GoogleOauthUtil googleOauthUtil;

    public GDriveController(GoogleOauthUtil googleOauthUtil) {
        this.googleOauthUtil = googleOauthUtil;
    }

    @GetMapping("/authorizationCode")
    public ResponseEntity<?> getAuthorizationCodeUrl(@RequestParam("redirectUrl") String redirectUrl) {
        String url = googleOauthUtil.getAuthorizationCodeUrl(GOOGLE_DRIVE_ID, redirectUrl);
        Map<String,String> ret = new HashMap<>();
        ret.put("url",url);
        return ResponseEntity.ok(ret);
    }

    @PostMapping("/token")
    public ResponseEntity<?> createToken(@RequestBody GoogleAuthResponse auth) {
        LOG.debug("In createToken " + auth);
        try {
            Credential credential = googleOauthUtil.getCredentialFromCode(auth, GOOGLE_DRIVE_ID);
            return ResponseEntity.ok(credential.getAccessToken());
        } catch (Exception e) {
            LOG.error("Problem retrieving credential!", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem creating token!");
    }

}
