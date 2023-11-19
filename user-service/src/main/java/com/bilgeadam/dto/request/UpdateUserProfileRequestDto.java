package com.bilgeadam.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserProfileRequestDto {

    private String token;

    private String username;

    private String email;

    private String phone;

    private String avatarUrl;

    private String address;

    private String about;
}
