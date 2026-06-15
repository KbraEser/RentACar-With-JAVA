package com.rentacar.service;

import com.rentacar.dto.CarSummaryDto;
import com.rentacar.dto.RentalRequest;
import com.rentacar.dto.RentalResponse;
import com.rentacar.entity.Car;
import com.rentacar.entity.Rental;
import com.rentacar.entity.User;
import com.rentacar.exceptions.ApiException;
import com.rentacar.repository.RentalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentalServiceImpl implements RentalService {

    private static final String ACTIVE_STATUS = "active";
    private static final String CANCELLED_STATUS = "cancelled";

    private final RentalRepository rentalRepository;
    private final CarService carService;

    public RentalServiceImpl(RentalRepository rentalRepository, CarService carService) {
        this.rentalRepository = rentalRepository;
        this.carService = carService;
    }

    @Override
    public RentalResponse create(RentalRequest request, User user) {
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new ApiException("Bitiş tarihi başlangıç tarihinden önce olamaz", HttpStatus.BAD_REQUEST);
        }

        Car car = carService.findById(request.getCarId());

        if (!Boolean.TRUE.equals(car.getIsAvailable())) {
            throw new ApiException("Bu araç şu anda kiralanabilir değil", HttpStatus.BAD_REQUEST);
        }

        boolean hasConflict = rentalRepository.existsByCarIdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                car.getId(),
                ACTIVE_STATUS,
                request.getEndDate(),
                request.getStartDate()
        );

        if (hasConflict) {
            throw new ApiException("Seçilen tarihlerde araç müsait değil", HttpStatus.CONFLICT);
        }

        Rental rental = new Rental();
        rental.setUser(user);
        rental.setCar(car);
        rental.setStartDate(request.getStartDate());
        rental.setEndDate(request.getEndDate());
        rental.setTotalPrice(request.getTotalPrice());
        rental.setCity(car.getCity());
        rental.setLocation(request.getLocation());
        rental.setStatus(ACTIVE_STATUS);

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
        return rentalRepository.findByCarIdAndStatus(carId, ACTIVE_STATUS);
    }

    @Override
    public RentalResponse cancel(Long rentalId, User user) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ApiException("Rezervasyon bulunamadı", HttpStatus.NOT_FOUND));

        if (!rental.getUser().getId().equals(user.getId())) {
            throw new ApiException("Bu rezervasyonu iptal etme yetkiniz yok", HttpStatus.FORBIDDEN);
        }

        if (CANCELLED_STATUS.equals(rental.getStatus())) {
            throw new ApiException("Rezervasyon zaten iptal edilmiş", HttpStatus.BAD_REQUEST);
        }

        rental.setStatus(CANCELLED_STATUS);
        return toResponse(rentalRepository.save(rental));
    }

    private RentalResponse toResponse(Rental rental) {
        Car car = rental.getCar();
        CarSummaryDto carSummary = new CarSummaryDto(car.getMake(), car.getModel(), car.getYear());

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
                carSummary
        );
    }
}
