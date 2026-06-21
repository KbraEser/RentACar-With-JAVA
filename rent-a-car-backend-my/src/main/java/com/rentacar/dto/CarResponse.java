package com.rentacar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CarResponse {
    private Long id;
    private String make;
    private String model;
    private int year;
    private BigDecimal pricePerDay;
    private String description;
    private String fuelType;
    private Integer seats;
    private boolean available;
    private String transmission;
    private boolean featured;
    private String city;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
