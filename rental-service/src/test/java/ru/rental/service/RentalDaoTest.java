package ru.rental.service;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rental.service.dao.RentalDao;
import ru.rental.service.model.Rental;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Тест RentalDao")
class RentalDaoTest extends BaseBd {

    @Autowired
    private RentalDao rentalDao;

    private static Stream<Arguments> sourceRental() {
        return Stream.of(
                Arguments.of(
                        Rental.builder()
                                .id(1)
                                .userId(1)
                                .carId(2)
                                .bikeId(null)
                                .startDate(LocalDateTime.parse("2025-03-01T10:00:00"))
                                .endDate(LocalDateTime.parse("2025-03-07T12:00:00"))
                                .rentalAmount(5000.00)
                                .isPaid(true)
                                .build()
                ),
                Arguments.of(
                        Rental.builder()
                                .id(2)
                                .userId(2)
                                .carId(1)
                                .bikeId(null)
                                .startDate(LocalDateTime.parse("2025-03-05T14:00:00"))
                                .endDate(LocalDateTime.parse("2025-03-10T09:00:00"))
                                .rentalAmount(4500.50)
                                .isPaid(false)
                                .build()
                ),
                Arguments.of(
                        Rental.builder()
                                .id(3)
                                .userId(3)
                                .carId(null)
                                .bikeId(2)
                                .startDate(LocalDateTime.parse("2025-02-20T09:00:00"))
                                .endDate(null)  // No end date
                                .rentalAmount(12000.00)
                                .isPaid(false)
                                .build()
                ),
                Arguments.of(
                        Rental.builder()
                                .id(4)
                                .userId(4)
                                .carId(4)
                                .bikeId(null)
                                .startDate(LocalDateTime.parse("2025-03-03T15:30:00"))
                                .endDate(LocalDateTime.parse("2025-03-08T18:00:00"))
                                .rentalAmount(7800.75)
                                .isPaid(true)
                                .build()
                )
        );
    }

    private static Stream<Arguments> sourceRentalForFilterTest() {
        return Stream.of(
                Arguments.of((Predicate<Rental>) rental -> rental.getUserId() == 1, 1),
                Arguments.of((Predicate<Rental>) rental -> rental.getCarId() != null && rental.getCarId() == 1, 1),
                Arguments.of((Predicate<Rental>) rental -> rental.getBikeId() != null && rental.getBikeId() == 2, 1),
                Arguments.of((Predicate<Rental>) Rental::getIsPaid, 2),
                Arguments.of((Predicate<Rental>) rental -> !rental.getIsPaid(), 2));
    }

    @Test
    @Description(value = "Тест проверяет возвращение всей аренды")
    @DisplayName("Тест возвращения всей аренды")
    void getAllRentalsTest() {
        List<Rental> rentals = rentalDao.getAll();
        assertTrue(rentals.size() > 0, "Должен быть хотя бы один rental в базе");
    }

    @Test
    @Description(value = "Тест проверяет была ли создана таблица аренды")
    @DisplayName("Тест существования таблицы аренды")
    void createTableTest() {
        rentalDao.createTable();
        boolean tableExists = rentalDao.checkIfTableExists("rentals");
        assertTrue(tableExists, "Таблица rentals должна существовать");

        boolean tableNotExists = rentalDao.checkIfTableExists("non_existing_table");
        assertFalse(tableNotExists, "Несуществующая таблица должна возвращать false");
    }

    @ParameterizedTest
    @MethodSource("sourceRental")
    @Description(value = "Тест проверяет возвращения аренды и что у нее правильные поля")
    @DisplayName("Test getRental")
    void getRentalTest(Rental sourceRental) {
        final var rental = rentalDao.get(sourceRental.getId());

        assertAll(
                () -> assertEquals(rental.getId(), sourceRental.getId()),
                () -> assertEquals(rental.getUserId(), sourceRental.getUserId()),
                () -> assertEquals(rental.getCarId(), sourceRental.getCarId()),
                () -> assertEquals(rental.getBikeId(), sourceRental.getBikeId()),
                () -> assertEquals(rental.getStartDate(), sourceRental.getStartDate()),
                () -> assertEquals(rental.getEndDate(), sourceRental.getEndDate()),
                () -> assertEquals(rental.getRentalAmount(), sourceRental.getRentalAmount()),
                () -> assertEquals(rental.getIsPaid(), sourceRental.getIsPaid())
        );
    }

    @ParameterizedTest
    @MethodSource("sourceRental")
    @Description(value = "Тест проверяет обновление аренды, а так же все ее поля на соответствие изменений")
    @DisplayName("Тест обновления аренды    ")
    void updateRentalTest(Rental sourceRental) {
        Rental updatedRental = Rental.builder()
                .id(sourceRental.getId())
                .userId(sourceRental.getUserId())
                .carId(sourceRental.getCarId())
                .bikeId(sourceRental.getBikeId())
                .startDate(LocalDateTime.now().minusDays(3))
                .endDate(LocalDateTime.now().minusHours(2))
                .rentalAmount(120.0)
                .isPaid(true)
                .build();

        Rental updatedRentalFromDb = rentalDao.update(sourceRental.getId(), updatedRental);

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
        Rental savedRental = rentalDao.save(sourceRental);

        assertNotNull(savedRental, "Аренда должна быть успешно сохранена");

        Rental rentalFromDb = rentalDao.get(savedRental.getId());
        assertEquals(savedRental.getId(), rentalFromDb.getId(), "ID аренды должен совпадать");

        assertTrue(rentalDao.delete(savedRental.getId()), "Аренда должна быть успешно удалена");
        assertFalse(rentalDao.delete(999), "Попытка удаления несуществующей аренды должна вернуть false");
    }


    @ParameterizedTest
    @MethodSource("sourceRentalForFilterTest")
    @Description(value = "Тест проверяет фильтрацию базы аренды по предикату")
    @DisplayName("Test filterRental")
    void filterRentalTest(Predicate<Rental> predicate, int expectedCount) {
        List<Rental> filteredRentals = rentalDao.filterBy(predicate);
        assertEquals(expectedCount, filteredRentals.size(), "Количество отфильтрованных rentals должно быть " + expectedCount);
        assertTrue(filteredRentals.stream().allMatch(predicate), "Все отфильтрованные записи должны соответствовать предикату");
    }
}
