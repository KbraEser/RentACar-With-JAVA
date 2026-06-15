package com.rentacar.repository;

import com.rentacar.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findByUserId(Long userId);

    List<Rental> findByCarIdAndStatus(Long carId, String status);

    List<Rental> findByCarIdInAndStatus(List<Long> carIds, String status);

    boolean existsByCarIdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long carId,
            String status,
            LocalDate endDate,
            LocalDate startDate
    );
}
