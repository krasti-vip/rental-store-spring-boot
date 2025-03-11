package ru.rental.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rental {

    private Integer id;

    private Integer userId;

    private Integer carId;

    private Integer bikeId;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Double rentalAmount;

    private Boolean isPaid;
}
