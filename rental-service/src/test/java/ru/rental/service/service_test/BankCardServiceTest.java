package ru.rental.service.service_test;

import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rental.service.BaseBd;
import ru.rental.service.dto.BankCardDto;
import ru.rental.service.service.BankCardService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        Optional<BankCardDto> cardDto = bankCardService.findById(cardId);

        assertTrue(cardDto.isPresent(), "Карта с cardId должна существовать");
        assertEquals("1234567809876543", cardDto.get().getNumberCard());
        assertTrue(bankCardService.findById(-1).isEmpty());
    }

    @Test
    @Description("Тест проверяет частичное обновление банковской карты через Map")
    @DisplayName("Тест bankCardUpdate() для частичного обновления карты")
    void bankCardUpdateTest() {

    }

    @Test
    @Description(value = "Тест проверяет сохранение и удаление банковской карты")
    @DisplayName("Тест save() и delete() для банковской карты")
    void saveAndDeleteTest() {

    }

    @Test
    @Description(value = "Тест проверяет получение всех банковских карт")
    @DisplayName("Тест getAll() для банковских карт")
    void getAllTest() {
        List<BankCardDto> cards = bankCardService.getAll();
        assertFalse(cards.isEmpty());
        assertEquals(5, cards.size());
    }
}
