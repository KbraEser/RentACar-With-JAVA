package com.rentacar.service;

import com.rentacar.dto.CarUpdateRequest;
import com.rentacar.entity.Car;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public interface CarService {

    List<Car>  findAll();

    List<Car> findFeatured();

    Car findById(Long id);

    Car update(Long id, CarUpdateRequest request);

    List<Car> findFiltered(
                           LocalDate startDate,
                           LocalDate endDate,
                           Double minPrice,
                           Double maxPrice,
                           String city,
                           String make,
                           String fuelType,
                           String transmission,
                           Integer seats,
                           Boolean isAvailable,
                           Boolean isFeatured
    );
}
