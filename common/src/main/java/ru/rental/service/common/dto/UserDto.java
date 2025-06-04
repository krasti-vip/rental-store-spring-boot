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

    private List<Integer> bankCards;

    private List<Integer> bikes;

    private List<Integer> cars;

    private List<Integer> bicycles;
}
