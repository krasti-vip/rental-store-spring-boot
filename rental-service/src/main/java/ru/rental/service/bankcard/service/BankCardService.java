package ru.rental.service.bankcard.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rental.service.bankcard.MapperUtilBankcard;
import ru.rental.service.common.dto.BankCardDto;
import ru.rental.service.common.dto.BankCardDtoCreate;
import ru.rental.service.bankcard.entity.BankCard;
import ru.rental.service.bankcard.repository.BankCardRepository;
import ru.rental.service.common.service.ServiceInterface;
import ru.rental.service.common.service.ServiceInterfaceUserId;
import ru.rental.service.user.entity.User;
import ru.rental.service.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BankCardService implements ServiceInterface<BankCardDto, BankCardDtoCreate>, ServiceInterfaceUserId<BankCardDto> {

    private final BankCardRepository bankCardRepository;

    private final UserRepository userRepository;

    private final MapperUtilBankcard mapperUtilBankcard;

    @Transactional(readOnly = true)
    public Optional<BankCardDto> findById(Integer id) {
        return bankCardRepository.findById(id)
                .map(mapperUtilBankcard::toDto);
    }

    @Transactional
    public BankCardDto create(BankCardDtoCreate bankCardDtoCreate) {
        BankCard bankCard = mapperUtilBankcard.toEntity(bankCardDtoCreate);
        BankCard savedCard = bankCardRepository.save(bankCard);

        return mapperUtilBankcard.toDto(savedCard);
    }

    @Transactional
    public BankCardDto update(BankCardDto updateBankCardDto) {
        BankCard existing = bankCardRepository.findById(updateBankCardDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("BankCard not found"));
        User user = userRepository.findById(updateBankCardDto.getUserId())
                        .orElseThrow(() -> new EntityNotFoundException("User not found"));

        existing.setNumberCard(updateBankCardDto.getNumberCard());
        existing.setExpirationDate(updateBankCardDto.getExpirationDate());
        existing.setSecretCode(updateBankCardDto.getSecretCode());
        existing.setUser(user);

        BankCard updated = bankCardRepository.save(existing);
        return mapperUtilBankcard.toDto(updated);
    }

    @Transactional
    public boolean delete(Integer id) {
        if (bankCardRepository.existsById(id)) {
            bankCardRepository.deleteById(id);

            return true;
        }

        return false;
    }

    @Transactional(readOnly = true)
    public List<BankCardDto> findByUserId(Integer userId) {
        return bankCardRepository.findByUserId(userId).stream()
                .map(mapperUtilBankcard::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BankCardDto> getAll() {
        Iterable<BankCard> cards = bankCardRepository.findAll();
        List<BankCard> cardList = new ArrayList<>();
        cards.forEach(cardList::add);

        return cardList.stream()
                .map(mapperUtilBankcard::toDto)
                .toList();
    }
}

