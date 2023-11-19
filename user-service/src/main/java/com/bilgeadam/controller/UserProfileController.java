package com.bilgeadam.controller;

import com.bilgeadam.dto.request.ActivateStatusRequestDto;
import com.bilgeadam.dto.request.DeleteStatusRequestDto;
import com.bilgeadam.dto.request.UpdateUserProfileRequestDto;
import com.bilgeadam.dto.request.UserCreateRequestDto;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.bilgeadam.constants.RestApi.*;
@RestController
@RequiredArgsConstructor
@RequestMapping(USER)
public class UserProfileController {

    private final UserProfileService userProfileService;
    @PostMapping(CREATE)
    public ResponseEntity<Boolean> createUser(@RequestBody UserCreateRequestDto dto){
        return ResponseEntity.ok(userProfileService.createUser(dto));
    }

    @GetMapping(ACTIVATESTATUS+"/{authId}")
    public ResponseEntity<Boolean> activateStatus(@PathVariable Long authId){
        return ResponseEntity.ok(userProfileService.activateStatus(authId));
    }

    @PostMapping(ACTIVATESTATUS2)
    public ResponseEntity<Boolean> activateStatus2(@RequestBody ActivateStatusRequestDto dto){
        return ResponseEntity.ok(userProfileService.activateStatus2(dto));
    }

    @PostMapping(UPDATE)
    public ResponseEntity<Boolean> updateProfile(@RequestBody UpdateUserProfileRequestDto dto){
        return ResponseEntity.ok(userProfileService.updateProfile(dto));
    }

    @PostMapping("/delete-status")
    public ResponseEntity<Boolean> deleteStatusUser(@RequestBody DeleteStatusRequestDto dto){
        return ResponseEntity.ok(userProfileService.deleteStatusUser(dto));
    }

}
