package com.rentacar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RentalResponse {
    private Long id;
    private Long userId;
    private Long carId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double totalPrice;
    private String status;
    private String city;
    private String location;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private CarSummaryDto cars;
}
