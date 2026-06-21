package com.rentacar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ActiveRentalResponse {
    private LocalDate startDate;
    private LocalDate endDate;
}
