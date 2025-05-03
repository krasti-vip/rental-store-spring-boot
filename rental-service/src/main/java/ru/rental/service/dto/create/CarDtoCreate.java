package ru.rental.service.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CarDtoCreate {

    @NotNull
    @NotBlank
    private String title;

    @NotNull
    private double price;

    @NotNull
    private Integer horsePower;

    @NotNull
    private double volume;

    @NotNull
    @NotBlank
    private String color;

    private Integer userId;
}
