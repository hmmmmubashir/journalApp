package com.engdigest.journalApp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Healthcheck {
    @GetMapping("/health-check")
    public String healthc(){
        return "working fine!!";
    }
}
