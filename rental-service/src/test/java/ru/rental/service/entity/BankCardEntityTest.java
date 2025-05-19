package ru.rental.service.entity;

import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BankCardEntityTest {

    @Test
    @Description(value = "Тест проверяет геттер и сеттор банковской карты")
    @DisplayName("Тест get() и set() bankCard")
    void getAndSetBankCardTest() {

        User user = new User(
                7,
                "user",
                "name",
                "test",
                123456L,
                null,
                null,
                null,
                null,
                null
        );

        BankCard bankCard = new BankCard(
                6,
                "1234567",
                "12/32",
                234,
                user
        );

        assertEquals(6, bankCard.getId());
        assertEquals(7, bankCard.getUser().getId());
        assertEquals("1234567", bankCard.getNumberCard());
        bankCard.setNumberCard("88888");
        assertEquals("88888", bankCard.getNumberCard());
    }
}
