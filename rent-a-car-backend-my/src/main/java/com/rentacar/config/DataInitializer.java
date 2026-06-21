package com.rentacar.config;

import com.rentacar.entity.Car;
import com.rentacar.enums.FuelType;
import com.rentacar.enums.Transmission;
import com.rentacar.repository.CarRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

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
                    createCar("Jeep", "Renegade", 2022, 1200.0, "SUV konforu", "Dizel", 5, "Otomatik", true, "İzmir"),
                    createCar("Toyota", "Yaris Cross", 2024, 1100.0, "Hybrid teknoloji ile ekonomik SUV", "Hybrid", 5, "Otomatik", true, "İstanbul"),
                    createCar("BMW", "X3", 2023, 2200.0, "Geniş bagajlı premium SUV", "Dizel", 5, "Otomatik", true, "Ankara"),
                    createCar("Mercedes", "E220d", 2022, 2400.0, "Uzun yol konforu sunan lüks sedan", "Dizel", 5, "Otomatik", false, "İzmir"),
                    createCar("Volkswagen", "Passat", 2023, 1300.0, "Aile ve iş seyahatleri için ideal", "Dizel", 5, "Otomatik", false, "Bursa"),
                    createCar("Tesla", "Model Y", 2024, 2500.0, "Elektrikli ve geniş iç hacimli SUV", "Elektrik", 5, "Otomatik", true, "Antalya"),
                    createCar("Ford", "Kuga", 2023, 1400.0, "Şehir içi ve dışı dengeli SUV", "Hybrid", 5, "Otomatik", false, "Adana"),
                    createCar("Honda", "CR-V", 2023, 1600.0, "Konforlu ve geniş aile SUV'u", "Benzin", 5, "Otomatik", true, "Gaziantep"),
                    createCar("Jeep", "Compass", 2022, 1350.0, "Sağlam yapılı kompakt SUV", "Dizel", 5, "Otomatik", false, "Konya"),
                    createCar("Nissan", "Qashqai", 2023, 1250.0, "Şehir SUV segmentinde popüler model", "Benzin", 5, "Otomatik", true, "Eskişehir"),
                    createCar("Volvo", "XC40", 2024, 2100.0, "Güvenlik odaklı premium crossover", "Hybrid", 5, "Otomatik", true, "İstanbul"),
                    createCar("Citroen", "C3 Aircross", 2022, 980.0, "Yumuşak sürüşlü pratik crossover", "Benzin", 5, "Manuel", false, "Ankara"),
                    createCar("Subaru", "Forester", 2023, 1700.0, "Dört tekerlekten çekişli güvenli SUV", "Benzin", 5, "Otomatik", false, "Antalya"),
                    createCar("Chery", "Tiggo 7", 2024, 1050.0, "Donanımlı ve uygun fiyatlı SUV", "Benzin", 5, "Otomatik", false, "Adana"),
                    createCar("SSangyong", "Korando", 2022, 1150.0, "Geniş iç hacimli ekonomik SUV", "Dizel", 5, "Otomatik", false, "Gaziantep"),
                    createCar("Toyota", "Camry Hybrid", 2023, 1650.0, "Uzun yolda yakıt tasarrufu sağlar", "Hybrid", 5, "Otomatik", true, "Konya"),
                    createCar("BMW", "520d", 2022, 2300.0, "İş seyahatleri için prestijli sedan", "Dizel", 5, "Otomatik", false, "Eskişehir"),
                    createCar("Mercedes", "GLA 200", 2024, 1950.0, "Şık ve kompakt premium SUV", "Benzin", 5, "Otomatik", true, "Bursa"),
                    createCar("Volkswagen", "T-Roc", 2023, 1280.0, "Genç sürücüler için dinamik crossover", "Benzin", 5, "Otomatik", false, "İzmir"),
                    createCar("Ford", "Puma", 2023, 1180.0, "Şehir içi pratik crossover model", "Benzin", 5, "Manuel", false, "Adana"),
                    createCar("Honda", "Jazz", 2022, 820.0, "Kompakt ve düşük maliyetli şehir aracı", "Benzin", 5, "Otomatik", false, "Eskişehir"),
                    createCar("Nissan", "Leaf", 2023, 1550.0, "Tamamen elektrikli şehir aracı", "Elektrik", 5, "Otomatik", true, "Ankara"),
                    createCar("Volvo", "S60", 2023, 2000.0, "Skandinav tasarımlı konforlu sedan", "Hybrid", 5, "Otomatik", false, "İstanbul")
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
        car.setPricePerDay(BigDecimal.valueOf(price));
        car.setDescription(description);
        car.setFuelType(FuelType.fromValue(fuelType.toLowerCase(Locale.ROOT)));
        car.setSeats(seats);
        car.setTransmission(Transmission.fromValue(transmission.toLowerCase(Locale.ROOT)));
        car.setFeatured(featured);
        car.setAvailable(true);
        car.setCity(city);
        return car;
    }
}
