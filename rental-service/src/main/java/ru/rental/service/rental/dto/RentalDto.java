package ru.rental.service.rental.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.rental.service.rental.entity.Rental;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RentalDto {

    @NotNull
    private Integer id;

    private Integer userId;

    private Integer carId;

    private Integer bikeId;

    private Integer bicycleId;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDate;

    @NotNull
    private Double rentalAmount;

    private Boolean isPaid;

    public static RentalDto toEntity(Rental rental) {
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
        if (rental.getBicycle() != null) {
            bicycleId = rental.getBicycle().getId();
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
}
