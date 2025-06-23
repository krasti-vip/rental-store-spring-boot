package ru.rental.service.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CarDtoCreate {

    private String title;

    private double price;

    private Integer horsePower;

    private double volume;

    private String color;

    private Integer userId;
}
