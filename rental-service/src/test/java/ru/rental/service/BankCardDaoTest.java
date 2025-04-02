package ru.rental.service;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rental.service.dao.BankCardDao;
import ru.rental.service.model.BankCard;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DisplayName("Тест BankCardDao")
class BankCardDaoTest extends BaseBd {

    @Autowired
    private BankCardDao bankCardDao;

    private static Stream<Arguments> sourceBankCard() {
        return Stream.of(
                Arguments.of(
                        BankCard.builder()
                                .id(1)
                                .userId(1)
                                .numberCard("1234567809876543")
                                .expirationDate("12/25")
                                .secretCode(123)
                                .build()
                ),
                Arguments.of(
                        BankCard.builder()
                                .id(2)
                                .userId(2)
                                .numberCard("4564567809876543")
                                .expirationDate("12/26")
                                .secretCode(345)
                                .build()
                ),
                Arguments.of(
                        BankCard.builder()
                                .id(3)
                                .userId(3)
                                .numberCard("9874567809876543")
                                .expirationDate("12/43")
                                .secretCode(543)
                                .build()
                ),
                Arguments.of(
                        BankCard.builder()
                                .id(4)
                                .userId(4)
                                .numberCard("7654567809876543")
                                .expirationDate("12/32")
                                .secretCode(567)
                                .build()
                ),
                Arguments.of(
                        BankCard.builder()
                                .id(5)
                                .userId(5)
                                .numberCard("6544567809876543")
                                .expirationDate("11/25")
                                .secretCode(346)
                                .build()
                )
        );
    }

    private static Stream<Arguments> sourceBankCardForFilterTest() {
        return Stream.of(
                Arguments.of(new BankCard(1, 1, "1234567809876543", "12/25", 123), 1, 1),
                Arguments.of(new BankCard(2, 2, "4564567809876543", "12/26", 345), 2, 2),
                Arguments.of(new BankCard(3, 3, "9874567809876543", "12/43", 543), 3, 3),
                Arguments.of(new BankCard(4, 4, "7654567809876543", "12/32", 567), 4, 4),
                Arguments.of(new BankCard(5, 5, "6544567809876543", "11/25", 346), 5, 5)
        );
    }

    @Test
    @Description(value = "Тест проверяет возвращения всех банковских карт")
    @DisplayName("Test getAllBankCard")
    void getAllBankCardTest() {
        final var allBankCard = bankCardDao.getAll();
        assertEquals(5, allBankCard.size());
    }

    @Test
    @Description(value = "Тест проверяет была ли создана таблица bank_cards")
    @DisplayName("Test создания Table bank_cards")
    void createTableTest() {
        bankCardDao.createTable();
        boolean creatTrue = bankCardDao.checkIfTableExists("bank_cards");
        assertTrue(creatTrue);
        boolean noCreat = bankCardDao.checkIfTableExists("test");
        assertFalse(noCreat);
    }

    @ParameterizedTest
    @MethodSource("sourceBankCard")
    @Description(value = "Тест проверяет возвращения банковских карт и что у них правильные поля")
    @DisplayName("Test getBankCard")
    void getBankCardTest(BankCard sourceBankCard) {
        final var bankCard = bankCardDao.get(sourceBankCard.getId());

        assertAll(
                () -> assertEquals(bankCard.getId(), sourceBankCard.getId()),
                () -> assertEquals(bankCard.getUserId(), sourceBankCard.getUserId()),
                () -> assertEquals(bankCard.getNumberCard(), sourceBankCard.getNumberCard()),
                () -> assertEquals(bankCard.getExpirationDate(), sourceBankCard.getExpirationDate()),
                () -> assertEquals(bankCard.getSecretCode(), sourceBankCard.getSecretCode())
        );
    }

    @ParameterizedTest
    @MethodSource("sourceBankCard")
    @Description(value = "Тест проверяет обновление банковских карт, а так же все их поля на соответствие изменений")
    @DisplayName("Тест обновления банковских карт")
    void updateBankCardTest(BankCard sourceBankCard) {

        BankCard updatedBankCard = new BankCard(
                sourceBankCard.getId(),
                sourceBankCard.getUserId(),
                "7654098756784321",
                "11/27",
                543
        );

        BankCard nonBankCard = new BankCard(
                -1,
                sourceBankCard.getUserId(),
                "7654098756784321",
                "11/27",
                543
        );

        int nonBankCardId = nonBankCard.getId();

        final var updatedBankCardBd = bankCardDao.update(sourceBankCard.getId(), updatedBankCard);
        final var bankCardUpdate = bankCardDao.get(updatedBankCardBd.getId());

        assertAll(
                () -> assertEquals(bankCardUpdate.getId(), updatedBankCard.getId()),
                () -> assertEquals(bankCardUpdate.getUserId(), updatedBankCard.getUserId()),
                () -> assertEquals(bankCardUpdate.getNumberCard(), updatedBankCard.getNumberCard()),
                () -> assertEquals(bankCardUpdate.getExpirationDate(), updatedBankCard.getExpirationDate()),
                () -> assertEquals(bankCardUpdate.getSecretCode(), updatedBankCard.getSecretCode()),
                () -> assertThrows(IllegalStateException.class, () -> {
                    bankCardDao.update(nonBankCardId, updatedBankCard);
                }, "Expected update to throw an exception as the BankCard does not exist")
        );
    }

    @ParameterizedTest
    @MethodSource("sourceBankCard")
    @Description(value = "Тест проверяет сохранение банковских карт, а затем его удаление и что количество велосипедов сначала " +
            "изменилось, а потом вернулась")
    @DisplayName("Тест сохранения и удаления банковских карт")
    void saveAndDeleteBankCardTest(BankCard sourceBankCard) {
        BankCard bankCardSave = new BankCard(
                -896,
                sourceBankCard.getUserId(),
                sourceBankCard.getNumberCard(),
                sourceBankCard.getExpirationDate(),
                sourceBankCard.getSecretCode()
        );

        BankCard savedBankCard = bankCardDao.save(bankCardSave);
        assertNotNull(savedBankCard, "банковская карта должна быть успешно сохранена");

        assertEquals(6, bankCardDao.getAll().size(), "После сохранения в базе должно быть 6 банковских карт");
        assertTrue(bankCardDao.getAll().stream().anyMatch(b -> b.getId() == savedBankCard.getId()), "Сохраненная банковская карта должна быть в списке");

        BankCard savedBankCardFromDb = bankCardDao.get(savedBankCard.getId());
        assertAll(
                () -> assertEquals(bankCardSave.getNumberCard(), savedBankCardFromDb.getNumberCard(), "Номер банковской карты должен совпадать"),
                () -> assertEquals(bankCardSave.getExpirationDate(), savedBankCardFromDb.getExpirationDate(), "Срок действия банковской карты должен совпадать"),
                () -> assertEquals(bankCardSave.getSecretCode(), savedBankCardFromDb.getSecretCode(), "Секретный код банковской карты должен совпадать")
        );

        assertTrue(bankCardDao.delete(savedBankCard.getId()), "банковская карта должна быть успешно удалена");
        assertFalse(bankCardDao.delete(999), "Попытка удаления несуществующей банковской карты должна вернуть false");
        assertEquals(5, bankCardDao.getAll().size(), "После удаления банковской карты в базе должно быть 5 банковских карт");
    }

    @ParameterizedTest
    @MethodSource("sourceBankCardForFilterTest")
    @Description(value = "Тест проверяет фильтрацию базы банковских карт по предикату")
    @DisplayName("Тест фильтрации банковских карт")
    void filtrBankCardTest(BankCard sourceBankCardForFilterTest, Integer filterKeyword, Integer expectedName) {

        Predicate<BankCard> predicate = a -> a.getUserId().equals(filterKeyword);

        List<BankCard> filteredBankCard = bankCardDao.filterBy(predicate);

        assertTrue(filteredBankCard.stream().anyMatch(b -> b.getUserId().equals(expectedName)),
                "Отфильтрованная банковская карта должна иметь номер " + expectedName);
    }

    @Test
    @DisplayName("Получение банковских карт для пользователя с id=3")
    void getAllByUserId_ForUser3_ShouldReturnCorrectCard() throws SQLException {
        int userId = 3;
        BankCard expectedCard = BankCard.builder()
                .id(3)  // ID будет сгенерировано БД, но мы знаем, что это 3-я запись
                .userId(3)
                .numberCard("9874567809876543")
                .expirationDate("12/43")
                .secretCode(543)
                .build();

        List<BankCard> result = bankCardDao.getAllByUserId(userId);

        // Проверки
        assertEquals(1, result.size(), "Должна быть ровно одна карта для пользователя с id=3");
        BankCard actualCard = result.get(0);

        assertAll("Проверка полей карты",
                () -> assertEquals(expectedCard.getUserId(), actualCard.getUserId(), "Не совпадает userId"),
                () -> assertEquals(expectedCard.getNumberCard(), actualCard.getNumberCard(), "Не совпадает номер карты"),
                () -> assertEquals(expectedCard.getExpirationDate(), actualCard.getExpirationDate(), "Не совпадает срок действия"),
                () -> assertEquals(expectedCard.getSecretCode(), actualCard.getSecretCode(), "Не совпадает секретный код")
        );
    }
}
