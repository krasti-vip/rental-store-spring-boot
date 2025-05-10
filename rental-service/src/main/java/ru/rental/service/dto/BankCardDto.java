package ru.rental.service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.CreditCardNumber;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BankCardDto {

    @NotNull
    private Integer id;

    @NotNull
    private Integer userId;

    @NotNull
    @NotBlank
    @CreditCardNumber
    private String numberCard;

    @NotNull
    @NotBlank
    private String expirationDate;

    @NotNull
    @Min(100)
    @Max(999)
    private Integer secretCode;
}
