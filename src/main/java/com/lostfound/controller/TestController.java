package com.lostfound.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "http://localhost:3000")
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from backend! Backend is running!";
    }

    @GetMapping("/status")
    public String status() {
        return "Status endpoint is working!";
    }
}
