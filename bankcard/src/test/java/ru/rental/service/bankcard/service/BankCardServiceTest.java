package ru.rental.service.bankcard.service;

import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ru.rental.service.bankcard.BaseBd;
import ru.rental.service.bankcard.entity.BankCard;
import ru.rental.service.bankcard.repository.BankCardRepository;
import ru.rental.service.bankcard.util.ConnectionManager;
import ru.rental.service.common.dto.BankCardDto;
import ru.rental.service.common.dto.BankCardDtoCreate;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тест BankCardService")
@SpringBootTest
class BankCardServiceTest extends BaseBd {

    @Autowired
    private BankCardService bankCardService;

    @Autowired
    private BankCardRepository bankCardRepository;

    @Test
    @Description(value = "Тест проверяет получение банковской карты по ID")
    @DisplayName("Тест findById() для банковской карты")
    void findByIdTest() {
        Integer cardId = bankCardService.getAll().get(0).getId();
        Optional<BankCardDto> cardDto = bankCardService.findById(cardId);

        assertEquals(3, cardDto.get().getUserId());
        assertTrue(cardDto.isPresent(), "Карта с cardId должна существовать");
        assertEquals("1234567809876543", cardDto.get().getNumberCard());
        assertTrue(bankCardService.findById(-1).isEmpty());
    }

    @Test
    @Description(value = "Тест проверяет сохранение и удаление банковской карты")
    @DisplayName("Тест create() и delete() для банковской карты")
    void createAndDeleteTest() {

        BankCardDtoCreate bankCardDtoCreate = new BankCardDtoCreate(
                null,
                "7654098756784321",
                "11/27",
                543
        );

        assertEquals(5, bankCardService.getAll().size());
        bankCardService.create(bankCardDtoCreate);
        List<BankCard> all = (List<BankCard>) bankCardRepository.findAll();

        assertEquals(6, all.size());

        assertTrue(bankCardService.delete(6));
        List<BankCard> all2 = (List<BankCard>) bankCardRepository.findAll();
        assertEquals(5, all2.size());
    }

    @Test
    @Description("Тест проверяет обновление банковской карты")
    @DisplayName("Тест bankCardUpdate() для обновления карты")
    void bankCardUpdateTest() {

        BankCardDto bankCardDto = new BankCardDto(
                3,
                2,
                "7654098756784321",
                "11/27",
                543
        );

        assertTrue(bankCardService.findByUserId(1).isEmpty());
        bankCardService.update(bankCardDto);
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
