package ru.rental.service.bankcard.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rental.service.bankcard.dto.BankCardDto;
import ru.rental.service.bankcard.dto.BankCardDtoCreate;
import ru.rental.service.bankcard.entity.BankCard;
import ru.rental.service.bankcard.repository.BankCardRepository;
import ru.rental.service.ServiceInterface;
import ru.rental.service.ServiceInterfaceUserId;
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

    @Transactional(readOnly = true)
    public Optional<BankCardDto> findById(Integer id) {
        return bankCardRepository.findById(id)
                .map(BankCardDto::toEntity);
    }

    @Transactional
    public BankCardDto create(BankCardDtoCreate bankCardDtoCreate) {
        User user = userRepository.findById(bankCardDtoCreate.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        BankCard bankCard = bankCardDtoCreate.toEntity(user);
        BankCard savedCard = bankCardRepository.save(bankCard);

        return BankCardDto.toEntity(savedCard);
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
        return BankCardDto.toEntity(updated);
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
                .map(BankCardDto::toEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BankCardDto> getAll() {
        Iterable<BankCard> cards = bankCardRepository.findAll();
        List<BankCard> cardList = new ArrayList<>();
        cards.forEach(cardList::add);

        return cardList.stream()
                .map(BankCardDto::toEntity)
                .toList();
    }
}

