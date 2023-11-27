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
public class RegisterElasticModel implements Serializable {

    private String id;

    private Long authId; // user tarafinda authId gerekiyor. orada direkt tüketebilmek icin buradan authId olarak gonderdik.

    private String username;

    private String email;
}
