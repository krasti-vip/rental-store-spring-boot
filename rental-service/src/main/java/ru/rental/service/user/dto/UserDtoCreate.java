package ru.rental.service.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rental.service.bicycle.dto.BicycleDto;
import ru.rental.service.bike.dto.BikeDto;
import ru.rental.service.car.dto.CarDto;
import ru.rental.service.bankcard.entity.BankCard;
import ru.rental.service.user.entity.User;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDtoCreate {

    @NotNull
    @NotBlank
    private String userName;

    @NotNull
    @NotBlank
    private String firstName;

    @NotNull
    @NotBlank
    private String lastName;

    @NotNull
    private Long passport;

    private String email;

    private List<BankCard> bankCards;

    private List<BikeDto> bikes;

    private List<CarDto> cars;

    private List<BicycleDto> bicycles;

    public User toEntity() {
        return User.builder()
                .userName(this.userName)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .passport(this.passport)
                .email(this.email)
                .bankCards(this.bankCards)
                .bikes(null)
                .cars(null)
                .bicycles(null)
                .build();
    }
}
