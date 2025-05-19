package ru.rental.service.entity;

import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BikeEntityTest {

    @Test
    @Description(value = "Тест проверяет геттер и сеттор мотоцикла")
    @DisplayName("Тест get() и set() bike")
    void getAndSetBikeTest() {

        User user = new User(
                6,
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

        Bike bike = new Bike(
                7,
                "Kok",
                54.5,
                75,
                0.750,
                user
        );

        assertEquals(7, bike.getId());
        assertEquals(6, bike.getUser().getId());
        assertEquals("Kok", bike.getName());
        bike.setName("JEYNE");
        assertEquals("JEYNE", bike.getName());
    }
}
