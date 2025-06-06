package ru.rental.service.bankcard.entity;

import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BankCardEntityTest {

    @Test
    @Description(value = "Тест проверяет геттер и сеттор банковской карты")
    @DisplayName("Тест get() и set() bankCard")
    void getAndSetBankCardTest() {

        BankCard bankCard = new BankCard(
                8,
                "1234567",
                "12/32",
                234,
                3
        );

        assertEquals(8, bankCard.getId());
        assertEquals(3, bankCard.getUserId());
        assertEquals("1234567", bankCard.getNumberCard());
        bankCard.setNumberCard("88888");
        assertEquals("88888", bankCard.getNumberCard());
        bankCard.setNumberCard("234888");
        assertEquals("234888", bankCard.getNumberCard());
    }
}
