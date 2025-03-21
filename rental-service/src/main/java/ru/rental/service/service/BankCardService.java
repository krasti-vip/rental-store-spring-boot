package ru.rental.service.service;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rental.service.dao.BankCardDao;
import ru.rental.service.dto.BankCardDto;
import ru.rental.service.model.BankCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Component
public class BankCardService implements Service<BankCardDto, Integer> {

    private static final Logger log = LoggerFactory.getLogger(BankCardService.class);

    private static final String NO_BANK_CARD_FOUND = "BankCard with id {} not found";

    private final ModelMapper modelMapper;

    private final BankCardDao bankCardDao;

    @Autowired
    public BankCardService(BankCardDao bankCardDao, ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.bankCardDao = bankCardDao;
    }

    @Override
    public Optional<BankCardDto> get(Integer id) {
        final var maybeBankCard = bankCardDao.get(id);

        if (maybeBankCard == null) {
            log.warn(NO_BANK_CARD_FOUND, id);
            return Optional.empty();
        } else {
            log.info("BankCard with id {} found", id);
            return Optional.of(modelMapper.map(maybeBankCard, BankCardDto.class));
        }
    }

    @Override
    public Optional<BankCardDto> update(Integer id, BankCardDto obj) {
        var maybeBankCard = bankCardDao.get(id);

        if (maybeBankCard == null) {
            log.warn(NO_BANK_CARD_FOUND, id);
            return Optional.empty();
        }

        var updatedBankCard = BankCard.builder()
                .id(maybeBankCard.getId())
                .userId(obj.getUserId())
                .numberCard(obj.getNumberCard())
                .expirationDate(obj.getExpirationDate())
                .secretCode(obj.getSecretCode())
                .build();

        var updated = bankCardDao.update(id, updatedBankCard);
        log.info("BankCard with id {} updated", id);
        return Optional.of(modelMapper.map(updated, BankCardDto.class));
    }

    @Override
    public BankCardDto save(BankCardDto obj) {
        var newBankCard = BankCard.builder()
                .userId(obj.getUserId())
                .numberCard(obj.getNumberCard())
                .expirationDate(obj.getExpirationDate())
                .secretCode(obj.getSecretCode())
                .build();

        var savedBankCard = bankCardDao.save(newBankCard);
        log.info("BankCard with id {} saved", savedBankCard.getId());
        return modelMapper.map(savedBankCard, BankCardDto.class);
    }

    @Override
    public boolean delete(Integer id) {
        var maybeBankCard = bankCardDao.get(id);

        if (maybeBankCard == null) {
            log.warn(NO_BANK_CARD_FOUND, id);
            return false;
        }
        log.info("BankCard with id {} deleted", id);
        return bankCardDao.delete(id);
    }

    @Override
    public List<BankCardDto> filterBy(Predicate<BankCardDto> predicate) {
        log.info("BankCard filtering by {}", predicate);
        return bankCardDao.getAll().stream()
                .map(e -> modelMapper.map(e, BankCardDto.class))
                .filter(predicate)
                .toList();
    }

    @Override
    public List<BankCardDto> getAll() {
        if (bankCardDao.getAll().isEmpty()) {
            log.info("No BankCards found");
            return new ArrayList<>();
        }
        log.info("BankCards found");
        return bankCardDao.getAll().stream()
                .map(e -> modelMapper.map(e, BankCardDto.class))
                .toList();
    }

    public List<BankCardDto> getAllByUserId(int userId) {
        log.info("Fetching bank cards for user with id {}", userId);
        return bankCardDao.getAllByUserId(userId).stream()
                .map(e -> modelMapper.map(e, BankCardDto.class))
                .toList();
    }
}

