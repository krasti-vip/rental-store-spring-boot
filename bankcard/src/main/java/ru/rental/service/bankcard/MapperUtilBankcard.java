package ru.rental.service.bankcard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rental.service.bankcard.entity.BankCard;
import ru.rental.service.common.dto.BankCardDto;
import ru.rental.service.common.dto.BankCardDtoCreate;

@RequiredArgsConstructor
@Component
public class MapperUtilBankcard {

    private final UserTemplate userTemplate;

    public BankCardDto toDto(BankCard bankCard) {
        Integer userId = null;

        if (bankCard.getUserId() != null) {
            userId = bankCard.getUserId();
        }

        return BankCardDto.builder()
                .id(bankCard.getId())
                .userId(userId)
                .numberCard(bankCard.getNumberCard())
                .expirationDate(bankCard.getExpirationDate())
                .secretCode(bankCard.getSecretCode())
                .build();
    }

    public BankCard toEntity(BankCardDtoCreate bankCardDtoCreate) {

        return BankCard.builder()
                .numberCard(bankCardDtoCreate.getNumberCard())
                .expirationDate(bankCardDtoCreate.getExpirationDate())
                .secretCode(bankCardDtoCreate.getSecretCode())
                .userId(bankCardDtoCreate.getUserId())
                .build();
    }
}
