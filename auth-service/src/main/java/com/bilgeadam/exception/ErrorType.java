package com.bilgeadam.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
    INTERNAL_ERROR(5100,"Sunucu Hatasi...", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST(4100,"Parametre Hatasi...", HttpStatus.BAD_REQUEST),
    LOGIN_ERROR(4110,"Kullanici adi veya sifre hatali...", HttpStatus.BAD_REQUEST),
    USERNAME_DUPLICATE(4111,"Kullanici adi kullanilmaktadir" ,HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(4112,"Kulanici bulunamadi..." , HttpStatus.BAD_REQUEST ),
    INVALID_TOKEN(4113,"Gecersiz token..." , HttpStatus.BAD_REQUEST ),
    USER_INACTIVE(4114,"Kullanici inaktif..." , HttpStatus.BAD_REQUEST ),
    ACTIVATION_CODE_ERROR(4115,"Aktivasyon kodu hatalidir..." , HttpStatus.FORBIDDEN ),
    TOKEN_NOT_CREATED(4116,"Token üretilirken bir hata oluştu" ,HttpStatus.INTERNAL_SERVER_ERROR );
    private int code;
    private String message;
    private HttpStatus httpStatus;
}
