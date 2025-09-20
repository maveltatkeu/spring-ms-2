package com.thecodealchemist.main.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GreetController {

    @GetMapping("/greet")
    public String greet() {
        return "Well, you're allowed";
    }
}