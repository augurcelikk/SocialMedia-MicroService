package com.bilgeadam.controller;

import com.bilgeadam.dto.request.*;
import com.bilgeadam.dto.response.LoginResponseDto;
import com.bilgeadam.dto.response.RegisterResponseDto;
import com.bilgeadam.repository.entity.Auth;
import com.bilgeadam.service.AuthService;
import com.bilgeadam.utility.JwtTokenManager;
import com.bilgeadam.utility.enums.ERole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.bilgeadam.constants.RestApi.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(AUTH)
public class AuthController {
    /*
        Auth'da activateStatus işlemini gerçekleştirdiğimizde user tarafı da bu güncellemeyi alsın ve statusu değişsin.

        Login metodumuzu duzenleyelim. bize bir token üretip bu tokeni dönsün. ayrıca statusu active olan kullanicilar giris yapabilsin.

        UserProfile için bir update metodu yazalım. Dto içerisine token alacak.
            Bu token'ın nereden geleceğini iyi düşünün. (Token'ın otomatik şekilde parametre olarak geçilmesine gerek yok, bir token elde edin,
            bu token'ı dto'ya parametre olarak swagger'da işleyin.)
            Update edilecek UserProfile'ı token ile yakalayalım ve update işlemlerini gerçekleştirelim.

            Hem orjinal, hem de yeni yazacağımız proje'de auth kısmına delete metodu ekleyelim.
            (delete metodu entity'i silmeyecek, status'u deleted'e çevirecek).
            Auth'da gerçekleştireceğimiz bu işlem userProfile'ı da güncellesin.

     */

    private final AuthService authService;
    private final JwtTokenManager tokenManager;


    @PostMapping(REGISTER)
    public ResponseEntity<RegisterResponseDto>  register (@RequestBody @Valid RegisterRequestDto dto){
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping(LOGIN)
    public ResponseEntity<String> login(@RequestBody LoginRequestDto dto){
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping(ACTIVATESTATUS)
    public ResponseEntity<Boolean> activateStatus(ActivationRequestDto dto){
        return ResponseEntity.ok(authService.activateStatus(dto));
    }

    @GetMapping(FINDALL)
    public ResponseEntity<List<Auth>> findAll(){
        return ResponseEntity.ok(authService.findAll());
    }

    @GetMapping("/create_token")
    public ResponseEntity<String> createToken(Long id, ERole role){
        return ResponseEntity.ok(tokenManager.createToken(id,role).get());
    }
    @GetMapping("/create_token2")
    public ResponseEntity<String> createToken2(Long id){
        return ResponseEntity.ok(tokenManager.createToken(id).get());
    }

    @GetMapping("/get_id_from_token")
    public ResponseEntity<Long> getIdFromToken(String token){
        return ResponseEntity.ok(tokenManager.getIdFromToken(token).get());
    }
    @GetMapping("/get_role_from_token")
    public ResponseEntity<String> getRoleFromToken(String token){
        return ResponseEntity.ok(tokenManager.getRoleFromToken(token).get());
    }

    @PostMapping("/deleted-status")
    public ResponseEntity<Boolean> deleteStatus(DeleteRequestDto dto){
        return ResponseEntity.ok(authService.deleteStatus(dto));
    }


}
