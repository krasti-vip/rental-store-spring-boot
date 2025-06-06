package ru.rental.service.car.entity;

import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CarEntityTest {

    @Test
    @Description(value = "Тест проверяет геттер и сеттор машины")
    @DisplayName("Тест get() и set() car")
    void getAndSetCarTest() {

        Car car = new Car(
                3,
                "Honda",
                548.5,
                750,
                2.750,
                "red",
                6
        );

        assertEquals(3, car.getId());
        assertEquals(6, car.getUserId());
        assertEquals("Honda", car.getTitle());
        car.setTitle("Toyota");
        assertEquals("Toyota", car.getTitle());
    }
}
