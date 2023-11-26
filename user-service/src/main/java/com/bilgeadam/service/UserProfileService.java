package com.bilgeadam.service;

import com.bilgeadam.dto.request.ActivateStatusRequestDto;
import com.bilgeadam.dto.request.DeleteStatusRequestDto;
import com.bilgeadam.dto.request.UpdateUserProfileRequestDto;
import com.bilgeadam.dto.request.UserCreateRequestDto;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.UserManagerException;
import com.bilgeadam.manager.AuthManager;
import com.bilgeadam.mapper.UserMapper;
import com.bilgeadam.rabbitmq.model.RegisterModel;
import com.bilgeadam.repository.UserProfileRepository;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.utility.JwtTokenManager;
import com.bilgeadam.utility.ServiceManager;
import com.bilgeadam.utility.enums.EStatus;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserProfileService extends ServiceManager<UserProfile,String> {

    private final UserProfileRepository userProfileRepository;
    private final JwtTokenManager jwtTokenManager;

    private final CacheManager cacheManager;

    private final AuthManager authManager;

    public UserProfileService(UserProfileRepository userProfileRepository, JwtTokenManager jwtTokenManager, CacheManager cacheManager, AuthManager authManager) {
        super(userProfileRepository);
        this.userProfileRepository = userProfileRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.cacheManager = cacheManager;
        this.authManager = authManager;
    }

    public Boolean createUser(UserCreateRequestDto dto) {
        try {
            save(UserMapper.INSTANCE.fromCreateRequestToUser(dto));
            return true;
        } catch (Exception e){
            throw new UserManagerException(ErrorType.USER_NOT_CREATED);
        }
    }

    public Boolean activateStatus(Long authId) {
      Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(authId);
      if(userProfile.isEmpty()){
          throw new UserManagerException(ErrorType.USER_NOT_FOUND);
      }else {
          userProfile.get().setStatus(EStatus.ACTIVE);
          update(userProfile.get());
          return true;
      }
    }

    public Boolean activateStatus2(ActivateStatusRequestDto dto) {
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(dto.getAuthId());
        if(userProfile.isEmpty()){
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }else {
            userProfile.get().setStatus(EStatus.ACTIVE);
            update(userProfile.get());
            return true;
        }
    }


    public Boolean updateProfile(UpdateUserProfileRequestDto dto) {
        Optional<Long> authId = jwtTokenManager.getIdFromToken(dto.getToken());
        if (authId.isEmpty()) {
            throw new UserManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(authId.get());
        if (userProfile.isEmpty()){
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }

        //userprofile'da degisiklik yapılması durumunda eski cache'i silelim.

        cacheManager.getCache("findbyusername").evict(userProfile.get().getUsername().toLowerCase());

        userProfile.get().setEmail(dto.getEmail());
        userProfile.get().setAbout(dto.getAbout());
        userProfile.get().setPhone(dto.getPhone());
        userProfile.get().setAddress(dto.getAddress());
        userProfile.get().setAvatarUrl(dto.getAvatarUrl());
        update(userProfile.get());
        return true;
    }


    public Boolean deleteStatusUser(DeleteStatusRequestDto dto) {
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(dto.getAuthId());
        if (userProfile.isEmpty()){
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }else {
            userProfile.get().setStatus(EStatus.DELETED);
            update(userProfile.get());
            return true;
        }
    }


    @Cacheable(value = "findbyusername", key = "#username.toLowerCase()")
    public UserProfile findByUsername(String username) { //uGUr -> ugur olarak repoya gitti.(asagidaki metot.)  uGUr -> ugur olarak cachelendi(key=#username yapisi)
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByUsernameIgnoreCase(username);
        if (userProfile.isEmpty()){
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        return userProfile.get();
    }

    @Cacheable(value = "findbyrole",key = "#role.toUpperCase()")
    public List<UserProfile> findByRole(String role){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        ResponseEntity<List<Long>> authIds = authManager.findByRole(role);
        List<Long> authIds = authManager.findByRole(role).getBody();


        return authIds.stream().map(x-> userProfileRepository.findOptionalByAuthId(x)
                .orElseThrow(()-> {throw new UserManagerException(ErrorType.USER_NOT_FOUND);})).collect(Collectors.toList()); //bir parametre kullanmiyoruz. hata fırlatacaksam parametresiz yollamalyiim. icerideki parametreyi napicam hatafirlatiyosam
    }


    public Boolean createUserWithRabbitMq(RegisterModel model) {
        try {
            save(UserMapper.INSTANCE.fromRegisterModelToUserProfile(model));

            //rabbitmq ile elastic'e gonder.
            return true;
        } catch (Exception e){
            throw new UserManagerException(ErrorType.USER_NOT_CREATED);
        }
    }



}
