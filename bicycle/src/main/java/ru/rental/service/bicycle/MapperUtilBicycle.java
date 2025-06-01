package ru.rental.service.bicycle;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rental.service.bicycle.entity.Bicycle;
import ru.rental.service.common.dto.BicycleDto;
import ru.rental.service.common.dto.BicycleDtoCreate;

@Component
@AllArgsConstructor
public class MapperUtilBicycle {

    private final UserTemplate userTemplate;

    public BicycleDto toDto(Bicycle bicycle) {
        Integer userId = null;
        if (bicycle.getUserId() != null) {
            userId = bicycle.getUserId();
        }
        return BicycleDto.builder()
                .id(bicycle.getId())
                .model(bicycle.getModel())
                .price(bicycle.getPrice())
                .color(bicycle.getColor())
                .userId(userId)
                .build();
    }

    public Bicycle toEntity(BicycleDtoCreate bicycleDtoCreate) {
        return Bicycle.builder()
                .model(bicycleDtoCreate.getModel())
                .price(bicycleDtoCreate.getPrice())
                .color(bicycleDtoCreate.getColor())
                .userId(bicycleDtoCreate.getUserId())
                .build();
    }
}
