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
    private String numberCard;

    @NotNull
    @NotBlank
    private String expirationDate;

    @NotNull
    @Min(100)
    @Max(999)
    private Integer secretCode;

    public static BankCardDto toEntity(BankCard bankCard) {
        Integer userId = null;
        if (bankCard.getUser() != null) {
            userId = bankCard.getUser().getId();
        }
        return BankCardDto.builder()
                .id(bankCard.getId())
                .userId(userId)
                .numberCard(bankCard.getNumberCard())
                .expirationDate(bankCard.getExpirationDate())
                .secretCode(bankCard.getSecretCode())
                .build();
    }
}
