package com.rentacar.dto;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;


@Data
public class RentalRequest {
    @NotNull(message = "Araç ID zorunludur.")
    private Long carId;

    @NotNull(message = "Başlangıç tarihi zorunludur.")
    @FutureOrPresent(message = "Başlangıç tarihi bugünden önce olamaz.")
    private LocalDate startDate;

    @NotNull(message = "Bitiş tarihi zorunludur.")
    private LocalDate endDate;

    @NotBlank(message = "Teslimat lokasyonu zorunludur.")
    private String location;
}

