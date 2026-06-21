package com.rentacar.repository;

import com.rentacar.entity.Car;
import com.rentacar.entity.Rental;
import com.rentacar.enums.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    @Query("SELECT r FROM Rental r WHERE r.user.id = :userId")
    List<Rental> findByUserId(@Param("userId") Long userId);

    @Query("SELECT r FROM Rental r WHERE r.car.id = :carId AND r.status = :status")
    List<Rental> findByCarIdAndStatus(
            @Param("carId") Long carId,
            @Param("status") RentalStatus status
    );

    @Query("SELECT r FROM Rental r WHERE r.car.id IN :carIds AND r.status = :status")
    List<Rental> findByCarIdInAndStatus(@Param("carIds")List<Long> carIds,@Param("status") RentalStatus status);

    @Query("""
         SELECT CASE WHEN COUNT(r)> 0 THEN true ELSE false END
         FROM Rental r
         WHERE r.car.id = :carId
         AND r.status = :status
         AND r.startDate <= :endDate
         AND r.endDate >= :startDate
         """)
    boolean existsByCarIdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            @Param("carId") Long carId,
            @Param("status") RentalStatus status,
            @Param("endDate") LocalDate endDate,
            @Param("startDate") LocalDate startDate
    );

}
