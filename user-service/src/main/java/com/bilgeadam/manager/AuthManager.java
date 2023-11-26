package com.bilgeadam.manager;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.bilgeadam.constants.RestApi.FINDBYROLE;


@FeignClient(url = "http://localhost:7070/api/v1/auth", name = "userprofile-auth")
public interface AuthManager {


    @GetMapping(FINDBYROLE)
    public ResponseEntity<List<Long>> findByRole(@RequestParam String role);

}
