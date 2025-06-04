package ru.rental.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rental.service.common.dto.UserDto;
import ru.rental.service.common.dto.UserDtoCreate;
import ru.rental.service.user.entity.User;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MapperUtilUser {

    public UserDto toDto(User user) {

        List<Integer> bikeDtos = null;
        if (user.getBikeId() != null) {
            bikeDtos = user.getBikeId();
        }

        List<Integer> carDtos = null;
        if (user.getCarsId() != null) {
            carDtos = user.getCarsId();
        }

        List<Integer> bicycleDtos = null;
        if (user.getBicyclesId() != null) {
            bicycleDtos = user.getBicyclesId();
        }

        List<Integer> bankcardDtos = null;
        if (user.getBankCardId() != null) {
            bankcardDtos = user.getBankCardId();
        }

        return UserDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .passport(user.getPassport())
                .email(user.getEmail())
                .bankCards(bankcardDtos)
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
                .bankCardId(null)
                .bikeId(null)
                .carsId(null)
                .bicyclesId(null)
                .build();
    }
}
