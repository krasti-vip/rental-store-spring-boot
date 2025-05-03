package ru.rental.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BicycleDto {

    @NotNull
    private Integer id;

    @NotNull
    private String model;

    @NotNull
    private double price;

    @NotNull
    private String color;

    private Integer userId;
}
