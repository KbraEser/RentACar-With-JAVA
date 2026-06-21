package com.rentacar.service;

import com.rentacar.dto.ActiveRentalResponse;
import com.rentacar.dto.CarSummaryDto;
import com.rentacar.dto.RentalRequest;
import com.rentacar.dto.RentalResponse;
import com.rentacar.entity.Car;
import com.rentacar.entity.Rental;
import com.rentacar.entity.User;
import com.rentacar.enums.RentalStatus;
import com.rentacar.exceptions.ApiException;
import com.rentacar.repository.RentalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RentalServiceImpl implements RentalService {

    private RentalRepository rentalRepository;
    private CarService carService;

    public RentalServiceImpl(RentalRepository rentalRepository, CarService carService) {
        this.rentalRepository = rentalRepository;
        this.carService = carService;
    }

    @Override
    @Transactional
    public RentalResponse create(RentalRequest request, User user) {

            if (request.getEndDate().isBefore(request.getStartDate())) {
                throw new ApiException("Bitiş tarihi başlangıç tarihinden önce olamaz", HttpStatus.BAD_REQUEST);
            }

            Car car = carService.findById(request.getCarId());

            if (!car.isAvailable()) {
                throw new ApiException("Seçilen araç şu anda müsait değil", HttpStatus.BAD_REQUEST);
            }

            boolean hasConflict = rentalRepository.existsByCarIdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                    request.getCarId(), RentalStatus.active, request.getEndDate(), request.getStartDate());

            if (hasConflict) {
                throw new ApiException("Seçilen tarihlerde araç zaten kiralanmış", HttpStatus.CONFLICT);
            }

            long rentalDays = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
            if (rentalDays <= 0) {
                throw new ApiException("Geçerli bir kiralama süresi seçin", HttpStatus.BAD_REQUEST);
            }

            BigDecimal totalPrice = car.getPricePerDay().multiply(BigDecimal.valueOf(rentalDays));

            Rental rental = new Rental();
            rental.setUser(user);
            rental.setCar(car);
            rental.setStartDate(request.getStartDate());
            rental.setEndDate(request.getEndDate());
            rental.setTotalPrice(totalPrice);
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
    public List<ActiveRentalResponse> findActiveByCarId(Long carId) {
        return rentalRepository.findByCarIdAndStatus(carId, RentalStatus.active).stream()
                .map(rental -> new ActiveRentalResponse(rental.getStartDate(), rental.getEndDate()))
                .toList();
    }

    @Override
    @Transactional
    public RentalResponse cancel(Long rentalId, User user) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ApiException("Rezervasyon bulunamadı: " + rentalId, HttpStatus.NOT_FOUND));

        if (!rental.getUser().getId().equals(user.getId())) {
            throw new ApiException("Bu rezervasyonu iptal etme yetkiniz yok", HttpStatus.FORBIDDEN);
        }

        if (rental.getStatus().equals(RentalStatus.cancelled)) {
            throw new ApiException("Bu rezervasyon zaten iptal edilmiş", HttpStatus.BAD_REQUEST);
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
