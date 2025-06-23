package ru.rental.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rental.service.common.dto.*;
import ru.rental.service.user.entity.User;
import java.util.function.Function;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MapperUtilUser {

    private final BankcardTemplate bankcardTemplate;
    private final BikeTemplate bikeTemplate;
    private final CarTemplate carTemplate;
    private final BicycleTemplate bicycleTemplate;

    public UserDto toDto(User user) {
        List<BankCardDto> bankCardDtos = convertIdsToDtos(user.getBankCardDtoId(), bankcardTemplate::findById);
        List<BikeDto> bikeDtos = convertIdsToDtos(user.getBikeDtoId(), bikeTemplate::findById);
        List<CarDto> carDtos = convertIdsToDtos(user.getCarDtoId(), carTemplate::findById);
        List<BicycleDto> bicycleDtos = convertIdsToDtos(user.getBicycleDtoId(), bicycleTemplate::findById);

        return UserDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .passport(user.getPassport())
                .email(user.getEmail())
                .bankCards(bankCardDtos)
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
                .bankCardDtoId(null)
                .bikeDtoId(null)
                .carDtoId(null)
                .bicycleDtoId(null)
                .build();
    }

    private <T> List<T> convertIdsToDtos(List<Integer> ids, Function<Integer, T> converter) {
        if (ids == null) {
            return null;
        }
        return ids.stream()
                .map(converter)
                .toList();
    }
}
