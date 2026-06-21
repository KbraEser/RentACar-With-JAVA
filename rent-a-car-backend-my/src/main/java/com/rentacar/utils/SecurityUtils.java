package com.rentacar.utils;

import com.rentacar.entity.User;
import com.rentacar.exceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    private SecurityUtils() {}

    public static User getCurrentUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof User user){
            return user;
        }
        throw new ApiException("Oturum açmış kullanıcı bulunamadı", HttpStatus.UNAUTHORIZED);
    }
}
