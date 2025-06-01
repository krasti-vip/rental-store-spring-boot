package ru.rental.service.rental;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rental.service.bike.entity.Bike;
import ru.rental.service.car.entity.Car;
import ru.rental.service.common.dto.RentalDto;
import ru.rental.service.common.dto.RentalDtoCreate;
import ru.rental.service.rental.entity.Rental;
import ru.rental.service.user.entity.User;

@Component
@RequiredArgsConstructor
public class MapperUtilRental {

        public RentalDto toDto(Rental rental) {
        Integer userId = null;
        if (rental.getUser() != null) {
            userId = rental.getUser().getId();
        }

        Integer carId = null;
        if (rental.getCar() != null) {
            carId = rental.getCar().getId();
        }

        Integer bikeId = null;
        if (rental.getBike() != null) {
            bikeId = rental.getBike().getId();
        }

        Integer bicycleId = null;
        if (rental.getBicycleId() != null) {
            bicycleId = rental.getBicycleId();
        }

        return RentalDto.builder()
                .id(rental.getId())
                .userId(userId)
                .carId(carId)
                .bikeId(bikeId)
                .bicycleId(bicycleId)
                .startDate(rental.getStartDate())
                .endDate(rental.getEndDate())
                .rentalAmount(rental.getRentalAmount())
                .isPaid(rental.getIsPaid())
                .build();
    }

        public Rental toEntity(RentalDtoCreate rentalDtoCreate, User user, Car car, Bike bike, Integer bicycleId) {
        return Rental.builder()
                .user(user)
                .car(car)
                .bike(bike)
                .bicycleId(bicycleId)
                .startDate(rentalDtoCreate.getStartDate())
                .endDate(rentalDtoCreate.getEndDate())
                .rentalAmount(rentalDtoCreate.getRentalAmount())
                .isPaid(rentalDtoCreate.getIsPaid())
                .build();
    }
}
