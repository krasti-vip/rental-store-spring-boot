package ru.rental.service.bike;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rental.service.bike.entity.Bike;
import ru.rental.service.common.dto.BikeDto;
import ru.rental.service.common.dto.BikeDtoCreate;
import ru.rental.service.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class MapperUtilBike {

    private final UserRepository userRepository;

        public BikeDto toDto(Bike bike) {
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

        public Bike toEntity(BikeDtoCreate bikeDtoCreate) {
        return Bike.builder()
                .name(bikeDtoCreate.getName())
                .price(bikeDtoCreate.getPrice())
                .horsePower(bikeDtoCreate.getHorsePower())
                .volume(bikeDtoCreate.getVolume())
                .user(userRepository.findById(bikeDtoCreate.getUserId()).orElseThrow(EntityNotFoundException::new))
                .build();
    }
}
