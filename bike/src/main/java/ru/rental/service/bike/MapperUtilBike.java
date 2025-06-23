package ru.rental.service.bike;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rental.service.bike.entity.Bike;
import ru.rental.service.common.dto.BikeDto;
import ru.rental.service.common.dto.BikeDtoCreate;

@Component
@RequiredArgsConstructor
public class MapperUtilBike {

    public BikeDto toDto(Bike bike) {
        Integer userId = null;

        if (bike.getUserId() != null) {
            userId = bike.getUserId();
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

    public Bike toEntity(BikeDtoCreate bikeDtoCreate) {

        return Bike.builder()
                .name(bikeDtoCreate.getName())
                .price(bikeDtoCreate.getPrice())
                .horsePower(bikeDtoCreate.getHorsePower())
                .volume(bikeDtoCreate.getVolume())
                .userId(bikeDtoCreate.getUserId())
                .build();
    }
}
