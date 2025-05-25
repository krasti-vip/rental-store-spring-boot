package ru.rental.service.entity;

import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.rental.service.car.entity.Car;
import ru.rental.service.user.entity.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CarEntityTest {

    @Test
    @Description(value = "Тест проверяет геттер и сеттор машины")
    @DisplayName("Тест get() и set() car")
    void getAndSetCarTest() {

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

        Car car = new Car(
                3,
                "Honda",
                548.5,
                750,
                2.750,
                "red",
                user
        );

        assertEquals(3, car.getId());
        assertEquals(6, car.getUser().getId());
        assertEquals("Honda", car.getTitle());
        car.setTitle("Toyota");
        assertEquals("Toyota", car.getTitle());
    }
}
