package com.rentacar.dto;

import java.math.BigDecimal;

public record CarUpdateRequest(
        BigDecimal pricePerDay,
        Boolean isAvailable,
        Boolean isFeatured
) {
}
