package com.rentacar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarSummaryDto {
    private String make;
    private String model;
    private Integer year;
}
