package com.luwings.springsecurityhttpbasicauth.controller;

import com.luwings.springsecurityhttpbasicauth.model.AuthenticatedUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/greet")
public class GreetController {

    @GetMapping
    public String greet() {
        //get the user from the security context holder
        AuthenticatedUser user = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return  "Hi " + user.getUsername() + ", you are allowed!";
    }

    @PostMapping("/update")
    public String updateResource() {
        AuthenticatedUser user = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return  "Hi " + user.getUsername() + ", you are allowed to update the resource!";
    }
}