package com.rentacar.service;

import com.rentacar.dto.RegisterRequest;
import com.rentacar.exceptions.ApiException;
import com.rentacar.repository.UserRepository;
import com.rentacar.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(RegisterRequest registerRequest) {
        if(!registerRequest.password().equals(registerRequest.passwordConfirm())){
            throw new ApiException("Şifreler eşleşmiyor", HttpStatus.BAD_REQUEST);
        }

        userRepository.findByEmail(registerRequest.email()).ifPresent(user -> {
            throw new ApiException("Bu email ile kayıtlı kullanıcı zaten var",HttpStatus.CONFLICT);
        });

        User user=new User();
        user.setName(registerRequest.name());
        user.setSurname(registerRequest.surname());
        user.setEmail(registerRequest.email());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));

        return userRepository.save(user);
    }
}
