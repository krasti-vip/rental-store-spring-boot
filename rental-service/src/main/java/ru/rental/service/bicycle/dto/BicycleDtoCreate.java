package ru.rental.service.bicycle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rental.service.bicycle.entity.Bicycle;
import ru.rental.service.user.entity.User;

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

    public Bicycle toEntity(User user) {
        return Bicycle.builder()
                .model(this.model)
                .price(this.price)
                .color(this.color)
                .user(user)
                .build();
    }
}
