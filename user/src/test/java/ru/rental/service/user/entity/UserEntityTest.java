package ru.rental.service.user.entity;

import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserEntityTest {

    @Test
    @Description(value = "Тест проверяет геттер и сеттор пользователя")
    @DisplayName("Тест get() и set() user")
    void getAndSetCarTest() {

        User user = new User(
                22,
                "user",
                "name",
                "test",
                123456L,
                "krasti@yandex.ru",
                null,
                null,
                null,
                null
        );

        assertEquals(22, user.getId());
        assertEquals("test", user.getLastName());
        user.setLastName("Tony");
        assertEquals("Tony", user.getLastName());
    }
}
