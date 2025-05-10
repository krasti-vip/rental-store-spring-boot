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
public class CarDto {

    @NotNull
    private Integer id;

    @NotNull
    private String title;

    @NotNull
    private double price;

    @NotNull
    private Integer horsePower;

    @NotNull
    private double volume;

    @NotNull
    private String color;

    private Integer userId;
}
