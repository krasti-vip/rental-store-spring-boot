package ru.rental.service.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rental.service.dto.BankCardDto;
import ru.rental.service.dto.create.BankCardDtoCreate;
import ru.rental.service.entity.BankCard;
import ru.rental.service.repository.BankCardRepository;
import ru.rental.service.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BankCardService implements ServiceInterface<BankCardDto, BankCardDtoCreate>, ServiceInterfaceUserId<BankCardDto> {

    private final BankCardRepository bankCardRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public Optional<BankCardDto> findById(Integer id) {
        return bankCardRepository.findById(id)
                .map(this::convertToDtoUser);
    }

    @Transactional
    public BankCardDto create(BankCardDtoCreate bankCardDtoCreate) {
        BankCard bankCard = modelMapper.map(bankCardDtoCreate, BankCard.class);
        setUserIfExists(bankCard, bankCardDtoCreate.getUserId());
        BankCard savedCard = bankCardRepository.save(bankCard);
        return convertToDtoUser(savedCard);
    }

    @Transactional
    public BankCardDto update(BankCardDto updateBankCardDto) {
        BankCard existingCard = bankCardRepository.findById(updateBankCardDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("BankCard not found"));
        modelMapper.map(updateBankCardDto, existingCard);
        setUserIfExists(existingCard, updateBankCardDto.getUserId());
        BankCard updatedCard = bankCardRepository.save(existingCard);

        return convertToDtoUser(updatedCard);
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
                .map(this::convertToDtoUser)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BankCardDto> getAll() {
        Iterable<BankCard> cards = bankCardRepository.findAll();
        List<BankCard> cardList = new ArrayList<>();
        cards.forEach(cardList::add);

        return cardList.stream()
                .map(this::convertToDtoUser)
                .toList();
    }

    private BankCardDto convertToDto(BankCard bankCard) {
        return modelMapper.map(bankCard, BankCardDto.class);
    }

    private BankCardDto convertToDtoUser(BankCard bankCard) {
        BankCardDto dto = convertToDto(bankCard);
        if (bankCard.getUser() != null) {
            dto.setUserId(bankCard.getUser().getId());
        }

        return dto;
    }

    private void setUserIfExists(BankCard bankCard, Integer userId) {
        userRepository.findById(userId).ifPresent(bankCard::setUser);
    }
}

