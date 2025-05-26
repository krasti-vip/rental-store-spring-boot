package ru.rental.service.car.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rental.service.car.entity.Car;
import ru.rental.service.user.entity.User;

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

    public Car toEntity(User user) {
        return Car.builder()
                .title(this.title)
                .price(this.price)
                .horsePower(this.horsePower)
                .volume(this.volume)
                .color(this.color)
                .user(user)
                .build();
    }
}
