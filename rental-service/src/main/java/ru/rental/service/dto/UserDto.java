package ru.rental.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rental.service.entity.BankCard;

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
}
