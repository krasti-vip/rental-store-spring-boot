package ru.rental.service.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BikeDto {

    private Integer id;

    private String name;

    private double price;

    private Integer horsePower;

    private double volume;

    private Integer userId;
}
