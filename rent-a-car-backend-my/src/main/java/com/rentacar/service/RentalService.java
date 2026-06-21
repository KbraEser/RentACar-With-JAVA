package com.rentacar.service;

import com.rentacar.dto.RentalRequest;
import com.rentacar.dto.RentalResponse;
import com.rentacar.entity.Rental;
import com.rentacar.entity.User;

import java.util.List;

public interface RentalService {
    RentalResponse create(RentalRequest request, User user);
    List<RentalResponse> findByUser(Long userId);
    List<Rental> findActiveByCarId(Long carId);
    RentalResponse cancel(Long rentalId, User user);
}
