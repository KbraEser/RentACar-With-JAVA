package com.rentacar.config;

import com.rentacar.entity.Car;
import com.rentacar.repository.CarRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedCars(CarRepository carRepository) {
        return args -> {
            if (carRepository.count() > 0) {
                return;
            }

            carRepository.saveAll(List.of(
                    createCar("Toyota", "Corolla", 2023, 850.0, "Ekonomik ve konforlu sedan", "Benzin", 5, "Otomatik", true, "İstanbul"),
                    createCar("BMW", "320i", 2024, 1500.0, "Premium sürüş deneyimi", "Benzin", 5, "Otomatik", true, "Ankara"),
                    createCar("Mercedes", "C200", 2023, 1800.0, "Lüks ve şık tasarım", "Benzin", 5, "Otomatik", true, "İzmir"),
                    createCar("Volkswagen", "Golf", 2022, 900.0, "Şehir içi kullanım için ideal", "Dizel", 5, "Manuel", false, "İstanbul"),
                    createCar("Tesla", "Model 3", 2024, 2000.0, "Elektrikli ve sessiz sürüş", "Elektrik", 5, "Otomatik", true, "Ankara"),
                    createCar("Ford", "Focus", 2021, 750.0, "Uygun fiyatlı aile aracı", "Benzin", 5, "Manuel", false, "Bursa"),
                    createCar("Honda", "Civic", 2023, 950.0, "Güvenilir ve ekonomik", "Benzin", 5, "Otomatik", false, "Antalya"),
                    createCar("Jeep", "Renegade", 2022, 1200.0, "SUV konforu", "Dizel", 5, "Otomatik", true, "İzmir")
            ));
        };
    }

    private Car createCar(
            String make,
            String model,
            int year,
            double price,
            String description,
            String fuelType,
            int seats,
            String transmission,
            boolean featured,
            String city
    ) {
        Car car = new Car();
        car.setMake(make);
        car.setModel(model);
        car.setYear(year);
        car.setPricePerDay(price);
        car.setDescription(description);
        car.setFuelType(fuelType);
        car.setSeats(seats);
        car.setTransmission(transmission);
        car.setIsFeatured(featured);
        car.setIsAvailable(true);
        car.setCity(city);
        return car;
    }
}
