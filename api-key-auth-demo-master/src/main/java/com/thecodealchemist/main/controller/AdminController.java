package com.thecodealchemist.main.controller;

import com.thecodealchemist.main.service.ApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Not using, just for the reference and practice
 */

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private ApiKeyService apiKeyService;

    @PostMapping("/generate-key/{owner}")
    public ResponseEntity<String> generateApiKey(@PathVariable String owner) {
        String apiKey = apiKeyService.generateNewKey(owner, 30); // Generate key with 30 days validity
        return ResponseEntity.ok("API Key generated: " + apiKey);
    }

    @PostMapping("/revoke-key/{key}")
    public ResponseEntity<String> revokeApiKey(@PathVariable String key) {
        boolean isRevoked = apiKeyService.revokeKey(key);
        return isRevoked ? ResponseEntity.ok("API Key revoked successfully.")
                : ResponseEntity.status(404).body("API Key not found.");
    }

    @PostMapping("/rotate-key/{key}")
    public ResponseEntity<String> rotateApiKey(@PathVariable String key) {
        String newApiKey = apiKeyService.rotateKeys(key);
        return newApiKey != null ? ResponseEntity.ok("API Key rotated successfully. New API Key: " + newApiKey)
                : ResponseEntity.status(404).body("API Key not found.");
    }
}

