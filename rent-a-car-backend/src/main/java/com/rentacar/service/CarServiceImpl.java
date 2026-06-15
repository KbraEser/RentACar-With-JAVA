package com.rentacar.service;

import com.rentacar.entity.Car;
import com.rentacar.entity.Rental;
import com.rentacar.exceptions.ApiException;
import com.rentacar.repository.CarRepository;
import com.rentacar.repository.RentalRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {

    private static final String ACTIVE_STATUS = "active";

    private final CarRepository carRepository;
    private final RentalRepository rentalRepository;

    public CarServiceImpl(CarRepository carRepository, RentalRepository rentalRepository) {
        this.carRepository = carRepository;
        this.rentalRepository = rentalRepository;
    }

    @Override
    public List<Car> findAll() {
        return carRepository.findAll();
    }

    @Override
    public List<Car> findFeatured() {
        return carRepository.findByIsFeaturedTrue();
    }

    @Override
    public Car findById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new ApiException("Araç bulunamadı", HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Car> findFiltered(
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
    ) {
        Specification<Car> spec = buildSpecification(
                minPrice, maxPrice, city, make, fuelType, transmission, seats, isAvailable, isFeatured
        );

        List<Car> cars = carRepository.findAll(spec);

        if (startDate != null && endDate != null) {
            return filterByDateRange(cars, startDate, endDate);
        }

        return cars;
    }

    private Specification<Car> buildSpecification(
            Double minPrice,
            Double maxPrice,
            String city,
            String make,
            String fuelType,
            String transmission,
            Integer seats,
            Boolean isAvailable,
            Boolean isFeatured
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("pricePerDay"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("pricePerDay"), maxPrice));
            }
            if (hasText(city)) {
                predicates.add(cb.equal(cb.lower(root.get("city")), city.toLowerCase()));
            }
            if (hasText(make)) {
                predicates.add(cb.equal(cb.lower(root.get("make")), make.toLowerCase()));
            }
            if (hasText(fuelType)) {
                predicates.add(cb.equal(cb.lower(root.get("fuelType")), fuelType.toLowerCase()));
            }
            if (hasText(transmission)) {
                predicates.add(cb.equal(cb.lower(root.get("transmission")), transmission.toLowerCase()));
            }
            if (seats != null) {
                predicates.add(cb.equal(root.get("seats"), seats));
            }
            if (isAvailable != null) {
                predicates.add(cb.equal(root.get("isAvailable"), isAvailable));
            }
            if (isFeatured != null) {
                predicates.add(cb.equal(root.get("isFeatured"), isFeatured));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private List<Car> filterByDateRange(List<Car> cars, LocalDate startDate, LocalDate endDate) {
        if (cars.isEmpty()) {
            return cars;
        }

        List<Long> carIds = cars.stream().map(Car::getId).toList();
        List<Rental> activeRentals = rentalRepository.findByCarIdInAndStatus(carIds, ACTIVE_STATUS);

        Set<Long> unavailableCarIds = activeRentals.stream()
                .filter(rental -> datesOverlap(rental.getStartDate(), rental.getEndDate(), startDate, endDate))
                .map(rental -> rental.getCar().getId())
                .collect(Collectors.toSet());

        return cars.stream()
                .filter(car -> !unavailableCarIds.contains(car.getId()))
                .toList();
    }

    private boolean datesOverlap(LocalDate rentalStart, LocalDate rentalEnd, LocalDate filterStart, LocalDate filterEnd) {
        return !rentalStart.isAfter(filterEnd) && !rentalEnd.isBefore(filterStart);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
