package ru.rental.service.repository_test;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rental.service.BaseBd;
import ru.rental.service.entity.BankCard;
import ru.rental.service.repository.BankCardRepository;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BankCardRepositoryTest extends BaseBd {

    @Autowired
    private BankCardRepository bankCardRepository;

    @Test
    @DisplayName("Тест возвращения всех карт")
    void findAllBankCardsTest() {
        List<BankCard> cards = (List<BankCard>) bankCardRepository.findAll();
        assertEquals(5, cards.size());
    }

    @Test
    @Transactional
    @DisplayName("Тест сохранения и удаление новой карты")
    void saveAndDeleteBankCardTest() {
        BankCard card = new BankCard(
                6,
                "7654098756784321",
                "11/27",
                543,
                null
        );

        bankCardRepository.save(card);
        List<BankCard> cards = (List<BankCard>) bankCardRepository.findAll();
        assertEquals(6, cards.size());
        bankCardRepository.deleteById(6);
        List<BankCard> cardsNew = (List<BankCard>) bankCardRepository.findAll();
        assertEquals(5, cardsNew.size());
    }

    @Test
    @DisplayName("Тест возвращение карты по id")
    void findBankCardByIdTest() {
        Optional<BankCard> card = bankCardRepository.findById(1);
        assertTrue(card.isPresent());
        assertEquals("1234567809876543", card.get().getNumberCard());
    }

    @Test
    @DisplayName("Тест обновление карты")
    void updateBankCardTest() {
        BankCard card = bankCardRepository.findById(1).orElseThrow();
        card.setNumberCard("88888888888");
        bankCardRepository.save(card);
        assertEquals(card, bankCardRepository.findById(1).get());
    }

    @Test
    @DisplayName("Тест получение карт User")
    void findCardsByUserTest() {
        List<BankCard> cards = bankCardRepository.findByUserId(1);
        assertEquals(1, cards.size());
    }
}
