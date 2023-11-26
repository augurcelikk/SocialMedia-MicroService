package com.bilgeadam.service;

import com.bilgeadam.dto.request.*;
import com.bilgeadam.dto.response.RegisterResponseDto;
import com.bilgeadam.exception.AuthManagerException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.manager.UserManager;
import com.bilgeadam.mapper.AuthMapper;
import com.bilgeadam.rabbitmq.producer.RegisterMailProducer;
import com.bilgeadam.rabbitmq.producer.RegisterProducer;
import com.bilgeadam.repository.AuthRepository;
import com.bilgeadam.repository.entity.Auth;
import com.bilgeadam.utility.CodeGenerator;
import com.bilgeadam.utility.JwtTokenManager;
import com.bilgeadam.utility.ServiceManager;
import com.bilgeadam.utility.enums.ERole;
import com.bilgeadam.utility.enums.EStatus;
import jakarta.transaction.Transactional;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthService extends ServiceManager<Auth,Long> {

    private final AuthRepository authRepository;
    private final JwtTokenManager tokenManager;
    private final UserManager userManager;

    private final CacheManager cacheManager;

    private final RegisterProducer registerProducer;

    private final RegisterMailProducer registerMailProducer;

    public AuthService(AuthRepository authRepository, UserManager userManager, JwtTokenManager tokenManager, CacheManager cacheManager, RegisterProducer registerProducer, RegisterMailProducer registerMailProducer) {
        super(authRepository);
        this.authRepository = authRepository;
        this.tokenManager = tokenManager;
        this.userManager = userManager;
        this.cacheManager = cacheManager;
        this.registerProducer = registerProducer;
        this.registerMailProducer = registerMailProducer;
    }


    @Transactional //metotta herhangi bir yerden exception donuyorsa metot icerisinde yapilan butun degisiklikleri geri alir. (RollBack)
    public RegisterResponseDto register(RegisterRequestDto dto) {
        Auth auth = AuthMapper.INSTANCE.fromRegisterRequestToAuth(dto);
        auth.setActivationCode(CodeGenerator.generateCode());

        try {
            save(auth);
            userManager.createUser(AuthMapper.INSTANCE.fromAuthToUserCreateRequestDto(auth));
            cacheManager.getCache("findbyrole").evict(auth.getRole().toString().toUpperCase());
        }catch (Exception e){
//            delete(auth);
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        return AuthMapper.INSTANCE.fromAuthToRegisterResponse(auth);
    }



    @Transactional //metotta herhangi bir yerden exception donuyorsa metot icerisinde yapilan butun degisiklikleri geri alir. (RollBack)
    public RegisterResponseDto registerWithRabbitMq(RegisterRequestDto dto) {
        Auth auth = AuthMapper.INSTANCE.fromRegisterRequestToAuth(dto);
        auth.setActivationCode(CodeGenerator.generateCode());

        try {
            save(auth);

            //rabbitmq ile haberlesme saglayacagiz.
//            registerProducer.sendNewUser(RegisterModel.builder()
//                            .authId(auth.getId())
//                            .email(auth.getEmail())
//                            .username(auth.getUsername())
//                    .build());

            registerProducer.sendNewUser(AuthMapper.INSTANCE.fromAuthToRegisterModel(auth));
            registerMailProducer.sendActivationCode(AuthMapper.INSTANCE.fromAuthToRegsiterMailModel(auth));

            cacheManager.getCache("findbyrole").evict(auth.getRole().toString().toUpperCase());
        }catch (Exception e){
//            delete(auth);
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        return AuthMapper.INSTANCE.fromAuthToRegisterResponse(auth);
    }




    public String login(LoginRequestDto dto) {
        Optional<Auth> authOptional =  authRepository.findOptionalByUsernameAndPassword(dto.getUsername(),dto.getPassword());
        if(authOptional.isEmpty()){
            throw new AuthManagerException(ErrorType.LOGIN_ERROR);
        }

        if (!(authOptional.get().getStatus().equals(EStatus.ACTIVE))){
            if (authOptional.get().getStatus().equals(EStatus.DELETED)){
                throw new AuthManagerException(ErrorType.DELETED_USER);
            } else if (authOptional.get().getStatus().equals(EStatus.BANNED)) {
                throw new AuthManagerException(ErrorType.BANNED_USER);
            } else{
                throw new AuthManagerException(ErrorType.USER_INACTIVE);
            }
        }
        Optional<String> token = tokenManager.createToken(authOptional.get().getId(),authOptional.get().getRole());
//        if (token.isEmpty())
//            throw new AuthManagerException(ErrorType.INVALID_TOKEN);

        return tokenManager.createToken(authOptional.get().getId(),authOptional.get().getRole()).orElseThrow(()-> {throw new AuthManagerException(ErrorType.TOKEN_NOT_CREATED);});
    }

    public Boolean activateStatus(ActivationRequestDto dto) {
        Optional<Auth> auth = findById(dto.getId());
        if(auth.isEmpty()) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        if(dto.getActivationCode().equals(auth.get().getActivationCode())){
            auth.get().setStatus(EStatus.ACTIVE);
            update(auth.get());
//            userManager.activateStatus(auth.get().getId());
            userManager.activateStatus2(ActivateStatusRequestDto.builder().authId(dto.getId()).build());
            //            auth.get().setUpdateDate(System.currentTimeMillis());
            //            authRepository.save(auth.get());
            return true;
        } else {
            throw new AuthManagerException(ErrorType.ACTIVATION_CODE_ERROR);
        }

    }

    public Boolean deleteStatus(DeleteRequestDto dto) {
        Optional<Auth> auth = findById(dto.getId());
        if(auth.isEmpty()) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        auth.get().setStatus(EStatus.DELETED);
        update(auth.get());

        userManager.deleteStatusUser(DeleteStatusRequestDto.builder()
                        .authId(dto.getId())
                .build());
        return true;
    }

    public List<Long> findByRole(String role) {
        ERole myRole;

        try {
            myRole = ERole.valueOf(role.toUpperCase(Locale.ENGLISH)); // admin-> ADMİN -> ADMIN
        }catch (Exception e){
            throw new AuthManagerException(ErrorType.ROLE_NOT_FOUND);
        }
        return authRepository.findAllByRole(myRole).stream().map(x->x.getId()).collect(Collectors.toList());
    }
}
