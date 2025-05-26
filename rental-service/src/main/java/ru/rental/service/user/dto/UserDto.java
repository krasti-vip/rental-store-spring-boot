package ru.rental.service.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rental.service.car.dto.CarDto;
import ru.rental.service.bicycle.dto.BicycleDto;
import ru.rental.service.bike.dto.BikeDto;
import ru.rental.service.bankcard.entity.BankCard;
import ru.rental.service.user.entity.User;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDto {

    @NotNull
    private Integer id;

    @NotNull
    private String userName;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private Long passport;

    @Email
    private String email;

    private List<BankCard> bankCards;

    private List<BikeDto> bikes;

    private List<CarDto> cars;

    private List<BicycleDto> bicycles;

    public static UserDto toEntity(User user) {
        List<BikeDto> bikeDtos = null;
        if (user.getBikes() != null) {
            bikeDtos = user.getBikes().stream()
                    .map(BikeDto::toEntity)
                    .toList();
        }

        List<CarDto> carDtos = null;
        if (user.getCars() != null) {
            carDtos = user.getCars().stream()
                    .map(CarDto::toEntity)
                    .toList();
        }

        List<BicycleDto> bicycleDtos = null;
        if (user.getBicycles() != null) {
            bicycleDtos = user.getBicycles().stream()
                    .map(BicycleDto::toEntity)
                    .toList();
        }

        return UserDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .passport(user.getPassport())
                .email(user.getEmail())
                .bankCards(user.getBankCards())
                .bikes(bikeDtos)
                .cars(carDtos)
                .bicycles(bicycleDtos)
                .build();
    }
}
