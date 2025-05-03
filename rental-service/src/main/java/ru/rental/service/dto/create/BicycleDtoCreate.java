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
public class BicycleDtoCreate {

    @NotNull
    @NotBlank
    private String model;

    @NotNull
    private double price;

    @NotNull
    @NotBlank
    private String color;

    private Integer userId;
}
