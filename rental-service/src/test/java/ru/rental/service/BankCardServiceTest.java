package ru.rental.service;

import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rental.service.dto.BankCardDto;
import ru.rental.service.service.BankCardService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тест BankCardService")
@SpringBootTest
class BankCardServiceTest extends BaseBd {

    @Autowired
    BankCardService bankCardService;

    @Test
    @Description(value = "Тест проверяет получение банковской карты по ID")
    @DisplayName("Тест get() для банковской карты")
    void getTest() {
        Integer cardId = bankCardService.getAll().get(0).getId();
        Optional<BankCardDto> cardDto = bankCardService.get(cardId);

        assertTrue(cardDto.isPresent(), "Карта с cardId должна существовать");
        assertEquals("1234567809876543", cardDto.get().getNumberCard());
        assertTrue(bankCardService.get(-1).isEmpty());
        assertThrows(IllegalArgumentException.class, () -> {
            bankCardService.get(null);
        });
    }

    @Test
    @Description(value = "Тест проверяет обновление банковской карты")
    @DisplayName("Тест update() для банковской карты")
    void updateTest() {
        Integer cardId = bankCardService.getAll().get(0).getId();
        BankCardDto updateDto = BankCardDto.builder()
                .userId(1)
                .numberCard("1111222233334444")
                .expirationDate("12/30")
                .secretCode(999)
                .build();

        Optional<BankCardDto> updatedCard = bankCardService.update(cardId, updateDto);

        assertTrue(updatedCard.isPresent());
        assertEquals("1111222233334444", updatedCard.get().getNumberCard());
        assertEquals(999, updatedCard.get().getSecretCode());
        assertTrue(bankCardService.update(-1, updateDto).isEmpty());
    }

    @Test
    @Description(value = "Тест проверяет сохранение и удаление банковской карты")
    @DisplayName("Тест save() и delete() для банковской карты")
    void saveAndDeleteTest() {
        int initialCount = bankCardService.getAll().size();

        BankCardDto newCard = BankCardDto.builder()
                .userId(5)
                .numberCard("9999888877776666")
                .expirationDate("01/27")
                .secretCode(321)
                .build();

        BankCardDto savedCard = bankCardService.save(newCard);
        assertNotNull(savedCard.getId());
        assertEquals(initialCount + 1, bankCardService.getAll().size());
        assertTrue(bankCardService.delete(savedCard.getId()));
        assertEquals(initialCount, bankCardService.getAll().size());
        assertFalse(bankCardService.delete(-1));
    }

    @Test
    @Description(value = "Тест проверяет получение всех банковских карт")
    @DisplayName("Тест getAll() для банковских карт")
    void getAllTest() {
        List<BankCardDto> cards = bankCardService.getAll();
        assertFalse(cards.isEmpty());
        assertEquals(5, cards.size());
    }

    @Test
    @Description(value = "Тест проверяет фильтрацию банковских карт")
    @DisplayName("Тест filterBy() для банковских карт")
    void filterTest() {
        List<BankCardDto> filteredCards = bankCardService.filterBy(card -> card.getUserId() == 1);
        assertEquals(1, filteredCards.size());
        assertEquals("1234567809876543", filteredCards.get(0).getNumberCard());
    }

    @Test
    @Description(value = "Тест проверяет получение карт по ID пользователя")
    @DisplayName("Тест getAllByUserId()")
    void getAllByUserIdTest() {
        List<BankCardDto> userCards = bankCardService.getAllByUserId(3);
        assertEquals(1, userCards.size());
        assertEquals("9874567809876543", userCards.get(0).getNumberCard());
        assertTrue(bankCardService.getAllByUserId(99).isEmpty());
    }
}
