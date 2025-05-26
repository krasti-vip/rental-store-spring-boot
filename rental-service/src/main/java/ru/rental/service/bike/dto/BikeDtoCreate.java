package ru.rental.service.bike.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rental.service.bike.entity.Bike;
import ru.rental.service.user.entity.User;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BikeDtoCreate {

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    private double price;

    @NotNull
    private Integer horsePower;

    @NotNull
    private double volume;

    private Integer userId;

    public Bike toEntity(User user) {
        return Bike.builder()
                .name(this.name)
                .price(this.price)
                .horsePower(this.horsePower)
                .volume(this.volume)
                .user(user)
                .build();
    }
}
