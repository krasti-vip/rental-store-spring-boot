package ru.rental.service.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rental.service.dto.BicycleDto;
import ru.rental.service.dto.BikeDto;
import ru.rental.service.dto.CarDto;

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

    private List<BikeDto> bikes;

    private List<CarDto> cars;

    private List<BicycleDto> bicycles;
}
