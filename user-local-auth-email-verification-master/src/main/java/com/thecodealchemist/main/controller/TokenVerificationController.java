package com.thecodealchemist.main.controller;

import com.thecodealchemist.main.service.TokenVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenVerificationController {
    @Autowired
    private TokenVerificationService tokenVerificationService;

    @PostMapping("/api/auth/verify")
    public ResponseEntity<String> verify(@RequestParam("token") String token) {
        if(tokenVerificationService.verify(token)) {
            return ResponseEntity.ok("token verified");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unidentified or invalid token");
    }
}
