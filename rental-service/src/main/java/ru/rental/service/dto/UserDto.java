package ru.rental.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rental.service.entity.Bicycle;
import ru.rental.service.entity.Bike;
import ru.rental.service.entity.Car;

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

    private Integer passport;

    private String email;

    private List<BikeDto> bikes;

    private List<CarDto> cars;

    private List<BicycleDto> bicycles;
}
