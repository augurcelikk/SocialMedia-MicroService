package com.bilgeadam.rabbitmq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterModel implements Serializable { //butun rabbitmq da gonderilecek seylerin serializable olmasi gerekiyor.

    private Long authId; // user tarafinda authId gerekiyor. orada direkt tüketebilmek icin buradan authId olarak gonderdik.

    private String username;

    private String email;
}
