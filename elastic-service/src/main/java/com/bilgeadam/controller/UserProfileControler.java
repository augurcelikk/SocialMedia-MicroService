package com.bilgeadam.controller;

import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.bilgeadam.constants.RestApi.FINDALL;
import static com.bilgeadam.constants.RestApi.USER;

@RestController
@RequiredArgsConstructor
@RequestMapping(USER)
public class UserProfileControler {

    private final UserProfileService userProfileService;

    @GetMapping(FINDALL)
    public ResponseEntity<Iterable<UserProfile>> findAll(){
        return ResponseEntity.ok(userProfileService.findAll());
    }

}
