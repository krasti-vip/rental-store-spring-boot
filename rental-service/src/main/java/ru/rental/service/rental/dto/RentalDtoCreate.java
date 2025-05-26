package ru.rental.service.rental.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.rental.service.bicycle.entity.Bicycle;
import ru.rental.service.bike.entity.Bike;
import ru.rental.service.car.entity.Car;
import ru.rental.service.rental.entity.Rental;
import ru.rental.service.user.entity.User;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RentalDtoCreate {

    @NotNull
    private Integer userId;

    private Integer carId;

    private Integer bikeId;

    private Integer bicycleId;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;

    @NotNull
    private Double rentalAmount;

    private Boolean isPaid;

    public Rental toEntity(User user, Car car, Bike bike, Bicycle bicycle) {
        return Rental.builder()
                .user(user)
                .car(car)
                .bike(bike)
                .bicycle(bicycle)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .rentalAmount(this.rentalAmount)
                .isPaid(this.isPaid)
                .build();
    }
}
