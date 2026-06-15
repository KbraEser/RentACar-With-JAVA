package com.rentacar.service;

import com.rentacar.dto.RegisterRequest;
import com.rentacar.entity.User;
import com.rentacar.exceptions.ApiException;
import com.rentacar.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterRequest request) {
        if (!request.password().equals(request.passwordConfirm())) {
            throw new ApiException("Şifreler eşleşmiyor", HttpStatus.BAD_REQUEST);
        }

        userRepository.findByEmail(request.email()).ifPresent(user -> {
            throw new ApiException("Bu email ile kayıtlı kullanıcı zaten var", HttpStatus.CONFLICT);
        });

        User user = new User();
        user.setName(request.name());
        user.setSurname(request.surname());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        return userRepository.save(user);
    }
}
