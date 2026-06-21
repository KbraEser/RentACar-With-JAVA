package com.rentacar.controller;

import com.rentacar.dto.ActiveRentalResponse;
import com.rentacar.dto.RentalRequest;
import com.rentacar.dto.RentalResponse;
import com.rentacar.entity.User;
import com.rentacar.service.RentalService;
import com.rentacar.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService){
        this.rentalService = rentalService;
    }

    @PostMapping
    public RentalResponse create(@Valid @RequestBody RentalRequest rentalRequest){
        User user = SecurityUtils.getCurrentUser();
        return rentalService.create(rentalRequest, user);
    }

    @GetMapping
    public List<RentalResponse> findMyRentals(){
        User user = SecurityUtils.getCurrentUser();
        return  rentalService.findByUser(user.getId());
    }

    @GetMapping("/car/{carId}/active")
    public List<ActiveRentalResponse> findActiveByCarId(@PathVariable Long carId) {
        return rentalService.findActiveByCarId(carId);
    }

    @PutMapping("/{id}/cancel")
    public RentalResponse cancel(@PathVariable Long id) {
        User user = SecurityUtils.getCurrentUser();
        return rentalService.cancel(id, user);
    }


}
