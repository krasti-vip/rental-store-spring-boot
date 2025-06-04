package ru.rental.service.rental;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rental.service.common.dto.RentalDto;
import ru.rental.service.common.dto.RentalDtoCreate;
import ru.rental.service.rental.entity.Rental;

@Component
@RequiredArgsConstructor
public class MapperUtilRental {

    public RentalDto toDto(Rental rental) {
        Integer userId = null;
        if (rental.getUserId() != null) {
            userId = rental.getUserId();
        }

        Integer carId = null;
        if (rental.getCarId() != null) {
            carId = rental.getCarId();
        }

        Integer bikeId = null;
        if (rental.getBikeId() != null) {
            bikeId = rental.getBikeId();
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

    public Rental toEntity(RentalDtoCreate rentalDtoCreate, Integer userId, Integer carId, Integer bikeId, Integer bicycleId) {
        return Rental.builder()
                .userId(userId)
                .carId(carId)
                .bikeId(bikeId)
                .bicycleId(bicycleId)
                .startDate(rentalDtoCreate.getStartDate())
                .endDate(rentalDtoCreate.getEndDate())
                .rentalAmount(rentalDtoCreate.getRentalAmount())
                .isPaid(rentalDtoCreate.getIsPaid())
                .build();
    }
}
