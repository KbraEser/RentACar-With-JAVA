package com.rentacar.repository;

import com.rentacar.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long>, JpaSpecificationExecutor<Car> {
    @Query("SELECT c FROM Car c WHERE c.isFeatured = true")
    List<Car> findByIsFeaturedTrue();
}
