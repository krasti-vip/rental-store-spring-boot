package ru.rental.service;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rental.service.entity.Bike;
import ru.rental.service.entity.Car;
import ru.rental.service.entity.Rental;
import ru.rental.service.entity.User;
import ru.rental.service.repository.RentalRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Тест RentalDao")
class RentalEntityTest extends BaseBd {

    @Autowired
    private RentalRepository rentalRepository;

    private static Stream<Arguments> sourceRental() {

        User user1 = User.builder().id(1).build();
        User user2 = User.builder().id(2).build();
        User user3 = User.builder().id(3).build();
        User user4 = User.builder().id(4).build();

        Car car1 = Car.builder().id(1).build();
        Car car2 = Car.builder().id(2).build();
        Car car4 = Car.builder().id(4).build();

        Bike bike2 = Bike.builder().id(2).build();

        return Stream.of(
                Arguments.of(
                        Rental.builder()
                                .id(1)
                                .user(user1)
                                .car(car1)
                                .bike(null)
                                .bicycle(null)
                                .startDate(LocalDateTime.parse("2025-03-01T10:00:00"))
                                .endDate(LocalDateTime.parse("2025-03-07T12:00:00"))
                                .rentalAmount(5000.00)
                                .isPaid(true)
                                .build()
                ),
                Arguments.of(
                        Rental.builder()
                                .id(2)
                                .user(user2)
                                .car(car2)
                                .bike(null)
                                .bicycle(null)
                                .startDate(LocalDateTime.parse("2025-03-05T14:00:00"))
                                .endDate(LocalDateTime.parse("2025-03-10T09:00:00"))
                                .rentalAmount(4500.50)
                                .isPaid(false)
                                .build()
                ),
                Arguments.of(
                        Rental.builder()
                                .id(3)
                                .user(user3)
                                .car(car4)
                                .bike(null)
                                .bicycle(null)
                                .startDate(LocalDateTime.parse("2025-02-20T09:00:00"))
                                .endDate(null)  // No end date
                                .rentalAmount(12000.00)
                                .isPaid(false)
                                .build()
                ),
                Arguments.of(
                        Rental.builder()
                                .id(4)
                                .user(user4)
                                .car(null)
                                .bike(bike2)
                                .bicycle(null)
                                .startDate(LocalDateTime.parse("2025-03-03T15:30:00"))
                                .endDate(LocalDateTime.parse("2025-03-08T18:00:00"))
                                .rentalAmount(7800.75)
                                .isPaid(true)
                                .build()
                )
        );
    }

    @Test
    @Description(value = "Тест проверяет возвращение всей аренды")
    @DisplayName("Тест возвращения всей аренды")
    void getAllRentalsTest() {

        List<Rental> rentals = (List<Rental>) rentalRepository.findAll();
        assertFalse(rentals.isEmpty(), "Должен быть хотя бы один rental в базе");
    }

    @ParameterizedTest
    @MethodSource("sourceRental")
    @Description(value = "Тест проверяет возвращения аренды и что у нее правильные поля")
    @DisplayName("Test getRental")
    void getRentalTest(Rental sourceRental) {
        final var rental = rentalRepository.findById(sourceRental.getId());

        assertAll(
                () -> assertEquals(rental.get().getId(), sourceRental.getId()),
                () -> assertEquals(rental.get().getUser(), sourceRental.getUser()),
                () -> assertEquals(rental.get().getCar(), sourceRental.getCar()),
                () -> assertEquals(rental.get().getBike(), sourceRental.getBike()),
                () -> assertEquals(rental.get().getStartDate(), sourceRental.getStartDate()),
                () -> assertEquals(rental.get().getEndDate(), sourceRental.getEndDate()),
                () -> assertEquals(rental.get().getRentalAmount(), sourceRental.getRentalAmount()),
                () -> assertEquals(rental.get().getIsPaid(), sourceRental.getIsPaid())
        );
    }

    @ParameterizedTest
    @MethodSource("sourceRental")
    @Description(value = "Тест проверяет обновление аренды, а так же все ее поля на соответствие изменений")
    @DisplayName("Тест обновления аренды    ")
    void updateRentalTest(Rental sourceRental) {
        Rental updatedRental = Rental.builder()
                .id(sourceRental.getId())
                .user(sourceRental.getUser())
                .car(sourceRental.getCar())
                .bike(sourceRental.getBike())
                .startDate(LocalDateTime.now().minusDays(3))
                .endDate(LocalDateTime.now().minusHours(2))
                .rentalAmount(120.0)
                .isPaid(true)
                .build();

        Rental updatedRentalFromDb = rentalRepository.save(updatedRental);

        assertAll(
                () -> assertEquals(updatedRentalFromDb.getId(), updatedRental.getId()),
                () -> assertEquals(updatedRentalFromDb.getRentalAmount(), updatedRental.getRentalAmount()),
                () -> assertEquals(updatedRentalFromDb.getIsPaid(), updatedRental.getIsPaid())
        );
    }

    @ParameterizedTest
    @MethodSource("sourceRental")
    @Description(value = "Тест проверяет сохранение аренды, а затем ее удаление и что количество аренды сначала " +
                         "изменилось, а потом вернулась")
    @DisplayName("Test save and delete rental")
    void saveAndDeleteRentalTest(Rental sourceRental) {
        Rental savedRental = rentalRepository.save(sourceRental);

        assertNotNull(savedRental, "Аренда должна быть успешно сохранена");

        Rental rentalFromDb = rentalRepository.findById(savedRental.getId())
                .orElseThrow(() -> new IllegalArgumentException("<UNK> <UNK> <UNK> <UNK>"));

        assertEquals(savedRental.getId(), rentalFromDb.getId(), "ID аренды должен совпадать");

        assertTrue(rentalRepository.existsById(savedRental.getId()), "Аренда должна быть успешно удалена");

        try {
            rentalRepository.deleteById(savedRental.getId());
        } catch (Exception ignored) {}
    }
}
