package ru.rental.service.bankcard.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rental.service.bankcard.entity.BankCard;
import ru.rental.service.user.entity.User;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BankCardDtoCreate {

    private Integer userId;

    @NotNull
    @NotBlank
    private String numberCard;

    @NotNull
    @NotBlank
    private String expirationDate;

    @NotNull
    @Min(100)
    @Max(999)
    private Integer secretCode;

    public BankCard toEntity(User user) {
        return BankCard.builder()
                .numberCard(this.numberCard)
                .expirationDate(this.expirationDate)
                .secretCode(this.secretCode)
                .user(user)
                .build();
    }
}
