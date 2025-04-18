package ru.rental.service.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rental.service.dto.BankCardDto;
import ru.rental.service.entity.BankCard;
import ru.rental.service.repository.BankCardRepository;
import ru.rental.service.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BankCardService {

    private final BankCardRepository bankCardRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public Optional<BankCardDto> findById(Integer id) {
        return bankCardRepository.findById(id)
                .map(this::convertToDtoWithUser);
    }

    @Transactional
    public BankCardDto create(BankCardDto bankCardDto) {
        BankCard bankCard = modelMapper.map(bankCardDto, BankCard.class);
        setUserIfExists(bankCard, bankCardDto.getUserId());
        BankCard savedCard = bankCardRepository.save(bankCard);
        return convertToDtoWithUser(savedCard);
    }

    @Transactional
    public Optional<BankCardDto> update(Integer id, BankCardDto bankCardDto) {
        return bankCardRepository.findById(id)
                .map(existingCard -> {
                    updateCardFields(existingCard, bankCardDto);
                    setUserIfExists(existingCard, bankCardDto.getUserId());
                    BankCard updatedCard = bankCardRepository.save(existingCard);
                    return convertToDtoWithUser(updatedCard);
                });
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
        return bankCardRepository.findById(userId).stream()
                .map(this::convertToDtoWithUser)
                .toList();
    }

    private BankCardDto convertToDto(BankCard bankCard) {
        return modelMapper.map(bankCard, BankCardDto.class);
    }

    private BankCardDto convertToDtoWithUser(BankCard bankCard) {
        BankCardDto dto = convertToDto(bankCard);
        if (bankCard.getUser() != null) {
            dto.setUserId(bankCard.getUser().getId());
        }

        return dto;
    }

    private void updateCardFields(BankCard bankCard, BankCardDto dto) {
        modelMapper.map(dto, bankCard);
    }

    private void setUserIfExists(BankCard bankCard, Integer userId) {
        if (userId != null) {
            userRepository.findById(userId).ifPresent(bankCard::setUser);
        }
    }

    @Transactional(readOnly = true)
    public List<BankCardDto> getAll() {
        Iterable<BankCard> cards = bankCardRepository.findAll();
        List<BankCard> cardList = new ArrayList<>();
        cards.forEach(cardList::add);

        return cardList.stream()
                .map(this::convertToDtoWithUser)
                .toList();
    }
}

