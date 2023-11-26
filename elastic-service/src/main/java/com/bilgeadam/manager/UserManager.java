package com.bilgeadam.manager;

import com.bilgeadam.repository.entity.UserProfile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static com.bilgeadam.constants.RestApi.FINDALL;


@FeignClient(url = "http://localhost:7071/api/v1/user", name = "elastic-userprofile")
public interface UserManager {

    @GetMapping(FINDALL)
    public ResponseEntity<List<UserProfile>> findAll();
}
