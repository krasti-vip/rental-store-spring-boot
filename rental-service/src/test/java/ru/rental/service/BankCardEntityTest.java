package ru.rental.service;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rental.service.entity.BankCard;
import ru.rental.service.entity.User;
import ru.rental.service.repository.BankCardRepository;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DisplayName("Тест BankCardDao")
class BankCardEntityTest extends BaseBd {

    @Autowired
    private BankCardRepository bankCardRepository;

    private static Stream<Arguments> sourceBankCard() {

        User user1 = User.builder().id(1).build();
        User user2 = User.builder().id(2).build();
        User user3 = User.builder().id(3).build();
        User user4 = User.builder().id(4).build();
        User user5 = User.builder().id(5).build();

        return Stream.of(
                Arguments.of(
                        BankCard.builder()
                                .id(1)
                                .user(user1)
                                .numberCard("1234567809876543")
                                .expirationDate("12/25")
                                .secretCode(123)
                                .build()
                ),
                Arguments.of(
                        BankCard.builder()
                                .id(2)
                                .user(user2)
                                .numberCard("4564567809876543")
                                .expirationDate("12/26")
                                .secretCode(345)
                                .build()
                ),
                Arguments.of(
                        BankCard.builder()
                                .id(3)
                                .user(user3)
                                .numberCard("9874567809876543")
                                .expirationDate("12/43")
                                .secretCode(543)
                                .build()
                ),
                Arguments.of(
                        BankCard.builder()
                                .id(4)
                                .user(user4)
                                .numberCard("7654567809876543")
                                .expirationDate("12/32")
                                .secretCode(567)
                                .build()
                ),
                Arguments.of(
                        BankCard.builder()
                                .id(5)
                                .user(user5)
                                .numberCard("6544567809876543")
                                .expirationDate("11/25")
                                .secretCode(346)
                                .build()
                )
        );
    }

    @Test
    @Description(value = "Тест проверяет возвращения всех банковских карт")
    @DisplayName("Test getAllBankCard")
    void getAllBankCardTest() {
        List<BankCard> allBankCard = (List<BankCard>) bankCardRepository.findAll();
        assertEquals(5, allBankCard.size());
    }

    @ParameterizedTest
    @MethodSource("sourceBankCard")
    @Description(value = "Тест проверяет возвращения банковских карт и что у них правильные поля")
    @DisplayName("Test getBankCard")
    void getBankCardTest(BankCard sourceBankCard) {
        final var bankCard = bankCardRepository.findById(sourceBankCard.getId());

        assertAll(
                () -> assertEquals(bankCard.get().getId(), sourceBankCard.getId()),
                () -> assertEquals(bankCard.get().getUser(), sourceBankCard.getUser()),
                () -> assertEquals(bankCard.get().getNumberCard(), sourceBankCard.getNumberCard()),
                () -> assertEquals(bankCard.get().getExpirationDate(), sourceBankCard.getExpirationDate()),
                () -> assertEquals(bankCard.get().getSecretCode(), sourceBankCard.getSecretCode())
        );
    }

    @ParameterizedTest
    @MethodSource("sourceBankCard")
    @Description(value = "Тест проверяет обновление банковских карт, а так же все их поля на соответствие изменений")
    @DisplayName("Тест обновления банковских карт")
    void updateBankCardTest(BankCard sourceBankCard) {

        BankCard updatedBankCard = new BankCard(
                sourceBankCard.getId(),
                "7654098756784321",
                "11/27",
                543,
                sourceBankCard.getUser()
        );

        BankCard updatedBankCardBd = bankCardRepository.save(sourceBankCard);
        final var bankCardUpdate = bankCardRepository.save(updatedBankCardBd);

        assertAll(
                () -> assertEquals(bankCardUpdate.getId(), updatedBankCard.getId()),
                () -> assertEquals(bankCardUpdate.getUser(), updatedBankCard.getUser()),
                () -> assertEquals(bankCardUpdate.getNumberCard(), updatedBankCard.getNumberCard()),
                () -> assertEquals(bankCardUpdate.getExpirationDate(), updatedBankCard.getExpirationDate()),
                () -> assertEquals(bankCardUpdate.getSecretCode(), updatedBankCard.getSecretCode())
        );
    }

    @ParameterizedTest
    @MethodSource("sourceBankCard")
    @Description(value = "Тест проверяет сохранение банковских карт, а затем его удаление и что количество велосипедов сначала " +
            "изменилось, а потом вернулась")
    @DisplayName("Тест сохранения и удаления банковских карт")
    void saveAndDeleteBankCardTest(BankCard sourceBankCard) {
        BankCard bankCardSave = new BankCard(
                99,
                sourceBankCard.getNumberCard(),
                sourceBankCard.getExpirationDate(),
                sourceBankCard.getSecretCode(),
                sourceBankCard.getUser()
        );

        BankCard savedBankCard = bankCardRepository.save(bankCardSave);
        assertNotNull(savedBankCard, "банковская карта должна быть успешно сохранена");

        List<BankCard> allBankCard = (List<BankCard>) bankCardRepository.findAll();

        assertEquals(6, allBankCard.size(), "После сохранения в базе должно быть 6 банковских карт");
        assertTrue(bankCardRepository.existsById(savedBankCard.getId()), "Сохраненная банковская карта должна быть в списке");

        assertAll(
                () -> assertEquals(bankCardSave.getNumberCard(), savedBankCard.getNumberCard(), "Номер банковской карты должен совпадать"),
                () -> assertEquals(bankCardSave.getExpirationDate(), savedBankCard.getExpirationDate(), "Срок действия банковской карты должен совпадать"),
                () -> assertEquals(bankCardSave.getSecretCode(), savedBankCard.getSecretCode(), "Секретный код банковской карты должен совпадать")
        );

        bankCardRepository.deleteById(savedBankCard.getId());
        assertFalse(bankCardRepository.existsById(savedBankCard.getId()), "Попытка удаления несуществующей банковской карты должна вернуть false");
        List<BankCard> allBankCard2 = (List<BankCard>) bankCardRepository.findAll();
        assertEquals(5, allBankCard2.size(), "После удаления банковской карты в базе должно быть 5 банковских карт");
    }
}
