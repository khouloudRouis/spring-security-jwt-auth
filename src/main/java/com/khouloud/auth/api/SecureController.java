package com.khouloud.auth.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecureController {

    @GetMapping("/api/secure")
    public ResponseEntity<String> secureEndpoint() {
        return ResponseEntity.ok("You have accessed a secure endpoint!");
    }

}
