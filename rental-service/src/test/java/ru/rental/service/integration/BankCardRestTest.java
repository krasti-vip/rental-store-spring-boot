package ru.rental.service.integration;

import io.qameta.allure.Description;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.rental.service.BaseBd;
import ru.rental.service.bankcard.controller.BankCardControllerRest;
import ru.rental.service.bankcard.dto.BankCardDto;
import ru.rental.service.bankcard.dto.BankCardDtoCreate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
class BankCardRestTest extends BaseBd {

    @Autowired
    private BankCardControllerRest bankCardControllerRest;

    @Test
    @Description(value = "Тест findById() для банковской карты")
    @DisplayName("Тест RestController для банковской карты")
    void findByIdTest() {
        ResponseEntity<BankCardDto> response = bankCardControllerRest.findById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        BankCardDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getId());
        assertEquals("1234567809876543", body.getNumberCard());
        assertEquals("12/25", body.getExpirationDate());
        assertEquals(123, body.getSecretCode());
        assertNotNull(body.getUserId());
        assertEquals(3, body.getUserId());
    }

    @Test
    @Description(value = "Тест проверяет получение всех банковских карт")
    @DisplayName("Тест findAll() для банковской карты через RestController")
    void findAllTest() {
        ResponseEntity<List<BankCardDto>> response = bankCardControllerRest.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<BankCardDto> bankCards = response.getBody();
        assertNotNull(bankCards);
        assertEquals(5, bankCards.size());
        boolean hasBill = bankCards.stream().anyMatch(u -> "9874567809876543".equals(u.getNumberCard()));
        assertTrue(hasBill, "В списке должна быть банковская карта с номером '9874567809876543'");
    }

    @Test
    @Description("Тест проверяет создание банковской карты через RestController")
    @DisplayName("Тест create() для банковской карты")
    void createTest() {
        BankCardDtoCreate newBankCard = new BankCardDtoCreate();
        newBankCard.setUserId(5);
        newBankCard.setNumberCard("23456789");
        newBankCard.setExpirationDate("12/32");
        newBankCard.setSecretCode(432);

        ResponseEntity<BankCardDto> response = bankCardControllerRest.create(newBankCard);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        BankCardDto created = response.getBody();
        assertNotNull(created);
        assertEquals(5, created.getUserId());
        assertEquals("23456789", created.getNumberCard());
        assertEquals("12/32", created.getExpirationDate());
        assertEquals(432, created.getSecretCode());
        assertTrue(created.getId() > 0);
    }

    @Test
    @Description("Тест проверяет удаление банковской карты через RestController")
    @DisplayName("Тест delete() для банковской карты")
    void deleteTest() {
        ResponseEntity<Void> response = bankCardControllerRest.delete(5);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        ResponseEntity<BankCardDto> afterDelete = bankCardControllerRest.findById(5);
        assertEquals(HttpStatus.NOT_FOUND, afterDelete.getStatusCode());
    }

    @Test
    @Description(value = "Тест проверяет обновление данных для банковской карты через RestController")
    @DisplayName("Тест updateUser() для банковской карты")
    void updateTest() {
        BankCardDto update = new BankCardDto();
        update.setId(1);
        update.setUserId(3);
        update.setNumberCard("23456789");
        update.setExpirationDate("12/32");
        update.setSecretCode(432);
        ResponseEntity<BankCardDto> response = bankCardControllerRest.update(1, update);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        BankCardDto updatedBankCard = response.getBody();
        assertNotNull(updatedBankCard);
        assertEquals(3, updatedBankCard.getUserId());
        assertEquals("23456789", updatedBankCard.getNumberCard());
        assertEquals("12/32", updatedBankCard.getExpirationDate());
        assertEquals(432, updatedBankCard.getSecretCode());
    }

    @Test
    @Description(value = "Тест проверяет валидацию при недопустимых значениях через RestController")
    @DisplayName("Тест updateBankCard() с неверными данными - ожидание ConstraintViolationException")
    void updateValidationErrorTest() {
        BankCardDto update = new BankCardDto();
        update.setNumberCard("");

        assertThrows(ConstraintViolationException.class, () -> {
            bankCardControllerRest.update(1, update);
        });
    }

    @Test
    @Description(value = "Тест проверяет получение банковской карты пользователя по его id через RestController")
    @DisplayName("Тест getByUserId() для банковской карты")
    void getByUserIdTest() {
        ResponseEntity<List<BankCardDto>> response = bankCardControllerRest.getByUserId(2);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<BankCardDto> bankCards = response.getBody();
        assertNotNull(bankCards);
        assertEquals(1, bankCards.size());
        assertEquals("4564567809876543", bankCards.get(0).getNumberCard());
    }
}
