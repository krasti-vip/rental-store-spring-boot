package ru.rental.service.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDto {

    private Integer id;

    private String userName;

    private String firstName;

    private String lastName;

    private Long passport;

    private String email;

    private List<BankCardDto> bankCards;

    private List<BikeDto> bikes;

    private List<CarDto> cars;

    private List<Integer> bicycles;
}
