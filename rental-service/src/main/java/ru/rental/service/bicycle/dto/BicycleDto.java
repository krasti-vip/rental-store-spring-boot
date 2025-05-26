package ru.rental.service.bicycle.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rental.service.bicycle.entity.Bicycle;

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

    public static BicycleDto toEntity(Bicycle bicycle) {
        Integer userId = null;
        if (bicycle.getUser() != null) {
            userId = bicycle.getUser().getId();
        }
        return BicycleDto.builder()
                .id(bicycle.getId())
                .model(bicycle.getModel())
                .price(bicycle.getPrice())
                .color(bicycle.getColor())
                .userId(userId)
                .build();
    }
}
