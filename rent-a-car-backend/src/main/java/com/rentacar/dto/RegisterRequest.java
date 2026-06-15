package com.rentacar.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Ad alanı boş bırakılamaz.")
        String name,
        @NotBlank(message = "Soyad alanı boş bırakılamaz.")
        String surname,
        @NotBlank(message = "Email alanı boş bırakılamaz.")
        @Email(message = "Geçerli bir email giriniz.")
        String email,
        @NotBlank(message = "Şifre alanı boş bırakılamaz.")
        @Size(min = 6, max = 20, message = "Şifre en az 6, en fazla 20 karakter olmalıdır.")
        String password,
        @NotBlank(message = "Şifre tekrarı boş bırakılamaz.")
        String passwordConfirm
) {
}
