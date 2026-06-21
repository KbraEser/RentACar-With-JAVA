package com.rentacar.service;

import com.rentacar.dto.CarSummaryDto;
import com.rentacar.dto.RentalRequest;
import com.rentacar.dto.RentalResponse;
import com.rentacar.entity.Car;
import com.rentacar.entity.Rental;
import com.rentacar.entity.User;
import com.rentacar.enums.RentalStatus;
import com.rentacar.repository.RentalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RentalServiceImpl  implements RentalService {

    private RentalRepository rentalRepository;
    private CarService carService;

    public RentalServiceImpl(RentalRepository rentalRepository, CarService carService) {
        this.rentalRepository = rentalRepository;
        this.carService = carService;
    }

    @Override
    public RentalResponse create(RentalRequest request, User user) {

            if(request.getEndDate().isBefore(request.getStartDate())){
                throw new RuntimeException();
            }

           Car car = carService.findById(request.getCarId());

            if(!car.isAvailable()){
                throw new RuntimeException();
            }

            boolean hasConflict = rentalRepository.existsByCarIdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                    request.getCarId(), RentalStatus.active,request.getEndDate(), request.getStartDate());

            if(hasConflict){
                throw new RuntimeException();
            }

            Rental rental = new Rental();
            rental.setUser(user);
            rental.setCar(car);
            rental.setStartDate(request.getStartDate());
            rental.setEndDate(request.getEndDate());
            rental.setTotalPrice(request.getTotalPrice());
            rental.setStatus(RentalStatus.active);
            rental.setCity(car.getCity());
            rental.setLocation(request.getLocation());

            return toResponse(rentalRepository.save(rental));
    }


    @Override
    public List<RentalResponse> findByUser(Long userId) {
       return rentalRepository.findByUserId(userId).stream()
               .map(this::toResponse)
               .toList();
    }

    @Override
    public List<Rental> findActiveByCarId(Long carId) {
        return rentalRepository.findByCarIdAndStatus(carId,RentalStatus.active);
    }

    @Override
    public RentalResponse cancel(Long rentalId, User user) {
        Rental rental = rentalRepository.findById(rentalId).orElse(null);

        if(!rental.getUser().getId().equals(user.getId())){
            throw new RuntimeException();
        }

        if(rental.getStatus().equals(RentalStatus.cancelled)){
            throw new RuntimeException();
        }

        rental.setStatus(RentalStatus.cancelled);
        return toResponse(rentalRepository.save(rental));
    }


    private RentalResponse toResponse(Rental rental){
        Car car = rental.getCar();
        CarSummaryDto carSummaryDto = new CarSummaryDto(car.getMake(),  car.getModel(), car.getYear());

        return new RentalResponse(
                rental.getId(),
                rental.getUser().getId(),
                car.getId(),
                rental.getStartDate(),
                rental.getEndDate(),
                rental.getTotalPrice(),
                rental.getStatus(),
                rental.getCity(),
                rental.getLocation(),
                rental.getCreatedAt(),
                rental.getUpdatedAt(),
                carSummaryDto
        );

    }
}
