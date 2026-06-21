package com.rentacar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String jwtToken;
    private Long userId;
    private String email;
    private String name;
    private String surname;
}
