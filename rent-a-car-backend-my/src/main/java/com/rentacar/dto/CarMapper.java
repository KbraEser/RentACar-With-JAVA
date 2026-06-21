package com.rentacar.dto;

import com.rentacar.entity.Car;

import java.util.List;

public final class CarMapper {

    private CarMapper() {
    }

    public static CarResponse toResponse(Car car) {
        return new CarResponse(
                car.getId(),
                car.getMake(),
                car.getModel(),
                car.getYear(),
                car.getPricePerDay(),
                car.getDescription(),
                car.getFuelType().getValue(),
                car.getSeats(),
                car.isAvailable(),
                car.getTransmission().getValue(),
                car.isFeatured(),
                car.getCity(),
                car.getCreatedAt(),
                car.getUpdatedAt()
        );
    }

    public static List<CarResponse> toResponseList(List<Car> cars) {
        return cars.stream().map(CarMapper::toResponse).toList();
    }
}
