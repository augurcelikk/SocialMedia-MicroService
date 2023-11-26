package com.bilgeadam.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/authservice")
    public ResponseEntity<String> authserviceFallback(){
       return ResponseEntity.ok("Authservice su anda hizmet disidir.");
    }

    @GetMapping("/userservice")
    public ResponseEntity<String> userserviceFallback(){
        return ResponseEntity.ok("Userservice su anda hizmet disidir.");
    }
}
