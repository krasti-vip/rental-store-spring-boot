package ru.rental.service.bike.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rental.service.bike.entity.Bike;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BikeDto {

    @NotNull
    private Integer id;

    @NotNull
    private String name;

    @NotNull
    private double price;

    @NotNull
    private Integer horsePower;

    @NotNull
    private double volume;

    private Integer userId;

    public static BikeDto toEntity(Bike bike) {
        Integer userId = null;
        if (bike.getUser() != null) {
            userId = bike.getUser().getId();
        }
        return BikeDto.builder()
                .id(bike.getId())
                .name(bike.getName())
                .price(bike.getPrice())
                .horsePower(bike.getHorsePower())
                .volume(bike.getVolume())
                .userId(userId)
                .build();

    }
}
