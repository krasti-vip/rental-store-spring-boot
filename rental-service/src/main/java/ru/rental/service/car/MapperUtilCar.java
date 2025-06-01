package ru.rental.service.car;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rental.service.car.entity.Car;
import ru.rental.service.common.dto.CarDto;
import ru.rental.service.common.dto.CarDtoCreate;
import ru.rental.service.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class MapperUtilCar {

    private final UserRepository userRepository;

        public CarDto toDto(Car car) {
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

        public Car toEntity(CarDtoCreate carDtoCreate) {
        return Car.builder()
                .title(carDtoCreate.getTitle())
                .price(carDtoCreate.getPrice())
                .horsePower(carDtoCreate.getHorsePower())
                .volume(carDtoCreate.getVolume())
                .color(carDtoCreate.getColor())
                .user(userRepository.findById(carDtoCreate.getUserId()).orElseThrow(EntityExistsException::new))
                .build();
    }
}
