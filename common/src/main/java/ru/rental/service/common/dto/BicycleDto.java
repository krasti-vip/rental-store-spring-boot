package ru.rental.service.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BicycleDto {

    private Integer id;

    private String model;

    private double price;

    private String color;

    private Integer userId;
}
