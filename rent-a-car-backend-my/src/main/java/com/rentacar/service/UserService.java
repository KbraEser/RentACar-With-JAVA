package com.rentacar.service;

import com.rentacar.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findById(long id);
}
