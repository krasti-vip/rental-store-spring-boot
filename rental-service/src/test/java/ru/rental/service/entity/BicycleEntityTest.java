package ru.rental.service.entity;

import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BicycleEntityTest {
    @Test
    @Description(value = "Тест проверяет геттер и сеттор велосипеда")
    @DisplayName("Тест get() и set() bicycle")
    void getAndSetBicycleTest() {

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

        Bicycle bicycle = new Bicycle(
                6,
                "Kok",
                54.5,
                "black",
                user
        );

        assertEquals(6, bicycle.getId());
        assertEquals(7, bicycle.getUser().getId());
        assertEquals("black", bicycle.getColor());
        bicycle.setColor("red");
        assertEquals("red", bicycle.getColor());
    }
}
