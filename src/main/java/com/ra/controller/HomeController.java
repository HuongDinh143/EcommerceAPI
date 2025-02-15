package com.ra.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HomeController {
    @GetMapping("/auth")
    public ResponseEntity<?> auth() {
        return new ResponseEntity<>("hello", HttpStatus.OK);
    }
    @GetMapping("/user/account")
    public ResponseEntity<?> userPage() {
        return new ResponseEntity<>("Trang người dùng", HttpStatus.OK);
    }
}
