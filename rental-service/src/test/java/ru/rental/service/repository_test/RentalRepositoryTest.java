package ru.rental.service.repository_test;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ru.rental.service.BaseBd;
import ru.rental.service.entity.Rental;
import ru.rental.service.repository.RentalRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class RentalRepositoryTest extends BaseBd {

    @Autowired
    private RentalRepository rentalRepository;

    @Test
    @DisplayName("Тест получения всей аренды")
    void findAllTest() {
        List<Rental> rentals = (List<Rental>) rentalRepository.findAll();
        assertEquals(4, rentals.size());
    }

    @Test
    @Transactional
    @DisplayName("Тест сохранения и удаления новой аренды")
    void saveAndDeleteTest() {
        Rental rental = new Rental(
                5,
                null,
                null,
                null,
                null,
                LocalDateTime.parse("2025-03-01 10:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.parse("2025-03-01 10:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                5432.98,
                true
        );
        rentalRepository.save(rental);
        List<Rental> rentals = (List<Rental>) rentalRepository.findAll();
        assertEquals(5, rentals.size());

        rentalRepository.deleteById(5);
        rentals = (List<Rental>) rentalRepository.findAll();
        assertEquals(4, rentals.size());
    }

    @Test
    @DisplayName("Тест возвращения аренды по id")
    void findByIdTest() {
        Rental rental = rentalRepository.findById(1).orElseThrow();
        assertEquals(true, rental.getIsPaid());
    }

    @Test
    @DisplayName("Тест обновление аренды")
    void updateRentalTest() {
        Rental rental = rentalRepository.findById(1).orElseThrow();
        rental.setIsPaid(false);
        rentalRepository.save(rental);
        assertEquals(rental, rentalRepository.findById(1).get());
    }

    @Test
    @DisplayName("Тест получение аренды User")
    void findRentalByUserTest() {
        List<Rental> rentals = rentalRepository.findAllByUserId(1);
        assertEquals(3, rentals.size());
    }
}
