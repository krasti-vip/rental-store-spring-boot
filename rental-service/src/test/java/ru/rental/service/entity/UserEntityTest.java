package ru.rental.service.entity;

import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.rental.service.car.entity.Car;
import ru.rental.service.user.entity.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserEntityTest {

    Car car = new Car(
            3,
            "Honda",
            548.5,
            750,
            2.750,
            "red",
            null
    );
    List<Car> cars = List.of(car);

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
                cars,
                null
        );

        assertEquals(22, user.getId());
        assertEquals(1, user.getCars().size());
        assertEquals("test", user.getLastName());
        user.setLastName("Tony");
        assertEquals("Tony", user.getLastName());
    }
}
