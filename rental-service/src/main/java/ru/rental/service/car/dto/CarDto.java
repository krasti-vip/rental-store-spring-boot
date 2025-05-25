package ru.rental.service.car.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rental.service.car.entity.Car;

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

    public static CarDto toEntity(Car car) {
        Integer userId = null;
        if(car.getUser() != null) {
            userId = car.getUser().getId();
        }

        return CarDto.builder()
                .id(car.getId())
                .title(car.getTitle())
                .price(car.getPrice())
                .horsePower(car.getHorsePower())
                .volume(car.getVolume())
                .color(car.getColor())
                .userId(userId)
                .build();
    }
}
