package ru.rental.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rental.service.bankcard.MapperUtilBankcard;
import ru.rental.service.bike.MapperUtilBike;
import ru.rental.service.car.MapperUtilCar;
import ru.rental.service.common.dto.*;
import ru.rental.service.user.entity.User;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MapperUtilUser {

    private final MapperUtilBankcard mapperUtilBankcard;

    private final MapperUtilBike mapperUtilBike;

    private final MapperUtilCar mapperUtilCar;

    public UserDto toDto(User user) {
        List<BikeDto> bikeDtos = null;
        if (user.getBikes() != null) {
            bikeDtos = user.getBikes().stream()
                    .map(mapperUtilBike::toDto)
                    .toList();
        }

        List<CarDto> carDtos = null;
        if (user.getCars() != null) {
            carDtos = user.getCars().stream()
                    .map(mapperUtilCar::toDto)
                    .toList();
        }

        List<Integer> bicycleDtos = null;
        if (user.getBicyclesId() != null) {
            bicycleDtos = user.getBicyclesId();
        }

        return UserDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .passport(user.getPassport())
                .email(user.getEmail())
                .bankCards(user.getBankCards().stream().map(mapperUtilBankcard::toDto).toList())
                .bikes(bikeDtos)
                .cars(carDtos)
                .bicycles(bicycleDtos)
                .build();
    }

    public User toEntity(UserDtoCreate userDtoCreate) {
        return User.builder()
                .userName(userDtoCreate.getUserName())
                .firstName(userDtoCreate.getFirstName())
                .lastName(userDtoCreate.getLastName())
                .passport(userDtoCreate.getPassport())
                .email(userDtoCreate.getEmail())
                .bankCards(userDtoCreate.getBankCards().stream().map(mapperUtilBankcard::toEntity).toList())
                .bikes(null)
                .cars(null)
                .bicyclesId(null)
                .build();
    }
}
