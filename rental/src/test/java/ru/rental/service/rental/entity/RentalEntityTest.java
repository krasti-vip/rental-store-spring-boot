package ru.rental.service.rental.entity;

import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rental.service.rental.BaseBd;
import ru.rental.service.rental.repository.RentalRepository;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class RentalEntityTest extends BaseBd {

    @Autowired
    private RentalRepository rentalRepository;

    @Test
    @Description(value = "isActive() возвращает true для аренды с endDate=null")
    @DisplayName("Тестирование метода активной аренды")
    void isActiveTest() {
        Optional<Rental> rentalOptional = rentalRepository.findById(3);
        assertTrue(rentalOptional.isPresent(), "Аренда должна существовать");
        Rental rental = rentalOptional.get();
        assertTrue(rental.isActive(), "Аренда должна быть активной");
    }

    @Test
    @Description(value = "Тест проверяет геттер и сеттор аренды")
    @DisplayName("Тест get() и set() rental")
    void getAndSetRentalTest() {

        Rental rental = new Rental(
                5,
                2,
                null,
                null,
                3,
                LocalDateTime.parse("2025-02-20T09:00:00"),
                LocalDateTime.parse("2025-03-10T09:00:00"),
                12_000.0,
                true
        );

        assertEquals(5, rental.getId());
        assertEquals(2, rental.getUserId());
        assertEquals(3, rental.getBicycleId());
        assertEquals(12_000.0, rental.getRentalAmount());
        rental.setRentalAmount(11_500.0);
        assertEquals(11_500.0, rental.getRentalAmount());
    }
}
