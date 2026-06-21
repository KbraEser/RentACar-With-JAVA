package com.rentacar.dto;

import com.rentacar.enums.RentalStatus;
import jakarta.persistence.EntityListeners;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
public class RentalResponse {
    private Long id;
    private Long userId;
    private Long carId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalPrice;
    private RentalStatus status;
    private String city;
    private String location;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private CarSummaryDto cars;
}
