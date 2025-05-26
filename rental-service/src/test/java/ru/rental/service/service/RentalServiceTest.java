package ru.rental.service.service;

import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rental.service.BaseBd;
import ru.rental.service.rental.dto.RentalDto;
import ru.rental.service.rental.dto.RentalDtoCreate;
import ru.rental.service.rental.repository.RentalRepository;
import ru.rental.service.rental.service.RentalService;
import ru.rental.service.user.service.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RentalServiceTest extends BaseBd {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private UserService userService;

    @Autowired
    private RentalRepository rentalRepository;

    @Test
    @Description(value = "Тест проверяет получение аренды по id")
    @DisplayName("Тест findBiId() для rental")
    void findBiId() {
        assertTrue(rentalService.findById(3).isPresent());
        assertEquals(1, rentalService.findById(4).get().getUserId());
    }

    @Test
    @Description(value = "Тест проверяет сохранение и удаление аренды")
    @DisplayName("Тест create() и delete() для аренды")
    void createAndDeleteTest() {
        RentalDtoCreate rental = new RentalDtoCreate(
                2,
                null,
                null,
                3,
                LocalDateTime.parse("2025-02-20T09:00:00"),
                null,
                543.9,
                false
        );

        assertEquals(4, rentalService.getAll().size());
        rentalService.create(rental);
        assertEquals(5, rentalService.getAll().size());
        rentalService.delete(5);
        assertEquals(4, rentalService.getAll().size());
    }

    @Test
    @Description("Тест проверяет обновление аренды")
    @DisplayName("Тест rentalUpdate() для аренды")
    void rentalUpdateTest() {
        RentalDto rental = new RentalDto(
                3,
                3,
                null,
                2,
                null,
                LocalDateTime.parse("2025-02-20T09:00:00"),
                LocalDateTime.parse("2025-03-10T09:00:00"),
                12_000.0,
                true
        );
        assertEquals(4, rentalService.getAll().size());
        assertFalse(rentalService.findById(3).get().getIsPaid());
        rentalService.update(rental);
        assertEquals(4, rentalService.getAll().size());
        assertTrue(rentalService.findById(3).get().getIsPaid());
    }

    @Test
    @Description(value = "Тест проверяет получение всей аренды пользователя")
    @DisplayName("Тест findByUserId() для аренды")
    void findByUserIdTest() {
        assertEquals(3, rentalService.findByUserId(1).size());
    }
}
