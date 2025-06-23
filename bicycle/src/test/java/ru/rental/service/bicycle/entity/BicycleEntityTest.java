package ru.rental.service.bicycle.entity;

import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BicycleEntityTest {
    @Test
    @Description(value = "Тест проверяет геттер и сеттор велосипеда")
    @DisplayName("Тест get() и set() bicycle")
    void getAndSetBicycleTest() {
        Bicycle bicycle = new Bicycle(
                6,
                "Kok",
                54.5,
                "black",
                3
        );

        assertEquals(6, bicycle.getId());
        assertEquals("black", bicycle.getColor());
        bicycle.setColor("red");
        assertEquals("red", bicycle.getColor());
    }
}
