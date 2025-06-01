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
public class UserDtoCreate {

    private String userName;

    private String firstName;

    private String lastName;

    private Long passport;

    private String email;

    private List<BankCardDtoCreate> bankCards;

    private List<BikeDtoCreate> bikes;

    private List<CarDtoCreate> cars;

    private List<Integer> bicycles;
}
