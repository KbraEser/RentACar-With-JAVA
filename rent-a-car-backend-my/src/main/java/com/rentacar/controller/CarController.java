package com.rentacar.controller;

import com.rentacar.dto.CarResponse;
import com.rentacar.dto.CarMapper;
import com.rentacar.service.CarService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public List<CarResponse> findAll() {
        return CarMapper.toResponseList(carService.findAll());
    }

    @GetMapping("/featured")
    public List<CarResponse> findFeatured() {
        return CarMapper.toResponseList(carService.findFeatured());
    }

    @GetMapping("/{id}")
    public CarResponse findById(@PathVariable Long id) {
        return CarMapper.toResponse(carService.findById(id));
    }

    @GetMapping("/filter")
    public List<CarResponse> findFiltered(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String make,
            @RequestParam(required = false) String fuelType,
            @RequestParam(required = false) String transmission,
            @RequestParam(required = false) Integer seats,
            @RequestParam(required = false) Boolean isAvailable,
            @RequestParam(required = false) Boolean isFeatured
    ) {
        return CarMapper.toResponseList(carService.findFiltered(
                startDate, endDate, minPrice, maxPrice, city, make,
                fuelType, transmission, seats, isAvailable, isFeatured
        ));
    }
}
