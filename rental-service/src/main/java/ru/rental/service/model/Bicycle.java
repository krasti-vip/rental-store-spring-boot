package ru.rental.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Bicycle {

    private int id;

    private String model;

    private double price;

    private String color;

    private Integer userId;
}
