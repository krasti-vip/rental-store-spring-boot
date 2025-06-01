package ru.rental.service.bankcard;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rental.service.bankcard.entity.BankCard;
import ru.rental.service.common.dto.BankCardDto;
import ru.rental.service.common.dto.BankCardDtoCreate;
import ru.rental.service.user.repository.UserRepository;

@RequiredArgsConstructor
@Component
public class MapperUtilBankcard {

    private final UserRepository userRepository;

    public BankCardDto toDto(BankCard bankCard) {
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

    public BankCard toEntity(BankCardDtoCreate bankCardDtoCreate) {
        return BankCard.builder()
                .numberCard(bankCardDtoCreate.getNumberCard())
                .expirationDate(bankCardDtoCreate.getExpirationDate())
                .secretCode(bankCardDtoCreate.getSecretCode())
                .user(userRepository.findById(bankCardDtoCreate.getUserId()).orElseThrow(EntityNotFoundException::new))
                .build();
    }
}
