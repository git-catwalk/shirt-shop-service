package com.bluntsoftware.shirtshop.integrations.quick_books.service;

import com.bluntsoftware.shirtshop.integrations.AuthResponse;
import org.springframework.http.ResponseEntity;
import java.util.Map;

public interface AuthService {
    Map<String,String> authUrl();
    ResponseEntity<?> token(AuthResponse auth);
    boolean credentialsExpired();
    ResponseEntity<?> refreshCredentials();
    boolean hasCredentials();
}
