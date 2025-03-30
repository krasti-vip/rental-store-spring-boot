package ru.rental.service;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rental.service.dao.BikeDao;
import ru.rental.service.dao.UserDao;
import ru.rental.service.model.Bike;
import ru.rental.service.model.User;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Тест BikeDao")
class BikeDaoTest extends BaseBd {

    @Autowired
    private BikeDao bikeDao;

    private static Stream<Arguments> sourceBike() {
        return Stream.of(
                Arguments.of(
                        Bike.builder()
                                .id(1)
                                .name("BMW")
                                .price(2000)
                                .horsePower(200)
                                .volume(1.0)
                                .build()
                ),
                Arguments.of(
                        Bike.builder()
                                .id(2)
                                .name("SUZUKI")
                                .price(30000)
                                .horsePower(300)
                                .volume(1.0)
                                .build()
                ),
                Arguments.of(
                        Bike.builder()
                                .id(3)
                                .name("YAMAHA")
                                .price(40000)
                                .horsePower(400)
                                .volume(1.0)
                                .build()
                ),
                Arguments.of(
                        Bike.builder()
                                .id(4)
                                .name("URAL")
                                .price(2000)
                                .horsePower(200)
                                .volume(1.0)
                                .build()
                ),
                Arguments.of(
                        Bike.builder()
                                .id(5)
                                .name("HONDA")
                                .price(2000)
                                .horsePower(200)
                                .volume(1.0)
                                .build()
                )
        );
    }

    private static Stream<Arguments> sourceBikeForFilterTest() {
        return Stream.of(
                Arguments.of(new Bike(1, "BMW", 2000, 200, 1.0, null), "URAL", "URAL"),
                Arguments.of(new Bike(2, "SUZUKI", 30000, 300, 1.0, null), "BMW", "BMW"),
                Arguments.of(new Bike(3, "YAMAHA", 40000, 400, 1.0, null), "YAMAHA", "YAMAHA"),
                Arguments.of(new Bike(4, "URAL", 2000, 200, 1.0, null), "URAL", "URAL"),
                Arguments.of(new Bike(5, "HONDA", 2000, 200, 1.0, null), "HONDA", "HONDA")
        );
    }

    @Test
    @Description(value = "Тест проверяет возвращения всех мотоциклов")
    @DisplayName("Test getAllBike")
    void bikeDaoGetAll() {
        final var allBikes = bikeDao.getAll();
        assertEquals(5, allBikes.size());
    }

    @Test
    @Description(value = "Тест проверяет была ли создана таблица bikes")
    @DisplayName("Test создания Table Bike")
    void createTableTest() {
        bikeDao.createTable();
        boolean creatTrue = bikeDao.checkIfTableExists("bikes");
        assertTrue(creatTrue);
        boolean noCreat = bikeDao.checkIfTableExists("test");
        assertFalse(noCreat);
    }

    @ParameterizedTest
    @MethodSource("sourceBike")
    @Description(value = "Тест проверяет возвращения байков и что у них правильные поля")
    @DisplayName("Test getBike")
    void getBikeTest(Bike sourceBike) {
        final var bike = bikeDao.get(sourceBike.getId());

        assertAll(
                () -> assertEquals(bike.getId(), sourceBike.getId()),
                () -> assertEquals(bike.getName(), sourceBike.getName()),
                () -> assertEquals(bike.getPrice(), sourceBike.getPrice()),
                () -> assertEquals(bike.getVolume(), sourceBike.getVolume()),
                () -> assertEquals(bike.getHorsePower(), sourceBike.getHorsePower())
        );
    }

    @ParameterizedTest
    @MethodSource("sourceBike")
    @Description(value = "Тест проверяет обновление байков, а так же все их поля на соответствие изменений")
    @DisplayName("Тест обновления мотоциклов")
    void updateBikerTest(Bike sourceBike) {

        Bike updatedBike = new Bike(
                sourceBike.getId(),
                sourceBike.getName(),
                sourceBike.getPrice() + 1000,
                sourceBike.getHorsePower() + 20,
                sourceBike.getVolume(),
                sourceBike.getUserId()
        );

        Bike nonBike = new Bike(
                -1,
                sourceBike.getName(),
                sourceBike.getPrice() + 1000,
                sourceBike.getHorsePower() + 20,
                sourceBike.getVolume(),
                sourceBike.getUserId()
        );

        int nonBikeId = nonBike.getId();

        final var updatedBikeBd = bikeDao.update(sourceBike.getId(), updatedBike);
        final var bikeUpdate = bikeDao.get(updatedBikeBd.getId());

        assertAll(
                () -> assertEquals(bikeUpdate.getId(), updatedBike.getId()),
                () -> assertEquals(bikeUpdate.getName(), updatedBike.getName()),
                () -> assertEquals(bikeUpdate.getPrice(), updatedBike.getPrice()),
                () -> assertEquals(bikeUpdate.getHorsePower(), updatedBike.getHorsePower()),
                () -> assertEquals(bikeUpdate.getVolume(), updatedBike.getVolume()),
                () -> assertThrows(IllegalStateException.class, () -> {
                    bikeDao.update(nonBikeId, updatedBike);
                }, "Expected update to throw an exception as the bike does not exist")
        );
    }

    @ParameterizedTest
    @MethodSource("sourceBike")
    @Description(value = "Тест проверяет сохранение мотоцикла, а затем его удаление и что количество мотоциклов сначала " +
                         "изменилось, а потом вернулась")
    @DisplayName("Тест сохранения и удаления мотоцикла")
    void saveAndDeleteBikeTest(Bike sourceBike) {
        Bike bikeToSave = new Bike(
                -896,
                sourceBike.getName(),
                sourceBike.getPrice(),
                sourceBike.getHorsePower(),
                sourceBike.getVolume(),
                sourceBike.getUserId()
        );

        Bike savedBike = bikeDao.save(bikeToSave);
        assertNotNull(savedBike, "Байк должен быть успешно сохранен");

        assertEquals(6, bikeDao.getAll().size(), "После сохранения в базе должно быть 6 байков");
        assertTrue(bikeDao.getAll().stream().anyMatch(b -> b.getId() == savedBike.getId()), "Сохраненный байк должен быть в списке");

        Bike savedBikeFromDb = bikeDao.get(savedBike.getId());
        assertAll(
                () -> assertEquals(bikeToSave.getName(), savedBikeFromDb.getName(), "Имя байка должно совпадать"),
                () -> assertEquals(bikeToSave.getPrice(), savedBikeFromDb.getPrice(), "Цена байка должна совпадать"),
                () -> assertEquals(bikeToSave.getHorsePower(), savedBikeFromDb.getHorsePower(), "Мощность байка должна совпадать"),
                () -> assertEquals(bikeToSave.getVolume(), savedBikeFromDb.getVolume(), "Объем байка должен совпадать")
        );

        assertTrue(bikeDao.delete(savedBike.getId()), "Байк должен быть успешно удален");
        assertFalse(bikeDao.delete(999), "Попытка удаления несуществующего байка должна вернуть false");
        assertEquals(5, bikeDao.getAll().size(), "После удаления байка в базе должно быть 5 байков");
    }

    @ParameterizedTest
    @MethodSource("sourceBikeForFilterTest")
    @Description(value = "Тест проверяет фильтрацию базы мотоциклов по предикату")
    @DisplayName("Тест фильтрации мотоциклов")
    void filtrBikeTest(Bike sourceBikeForFilterTest, String filterKeyword, String expectedName) {

        Predicate<Bike> predicate = bike -> bike.getName().contains(filterKeyword);

        List<Bike> filteredBikes = bikeDao.filterBy(predicate);

        assertTrue(filteredBikes.stream().anyMatch(b -> b.getName().equals(expectedName)),
                "Отфильтрованный байк должен иметь модель " + expectedName);
    }

    @Test
    @Description(value = "Тест проверяет обновление списка мотоциклов у пользователя, а так же что данный мотоцикл принадлежит" +
                         "определенному пользователю")
    @DisplayName("Тест добавление аренды мотоцикла")
    void updateUserId() {
        UserDao userDao = new UserDao();
        bikeDao.updateUserId(2, 5);
        User user = userDao.get(5);
        assertNotNull(user, "Пользователь с ID 5 не найден");
        List<Bike> userBikes = bikeDao.getAllByUserId(user.getId());
        assertNotNull(userBikes, "Список байков у пользователя не должен быть null");
        assertFalse(userBikes.isEmpty(), "Список bikes у пользователя не должен быть пустым");
        for (Bike bike : userBikes) {
            System.out.println("Bike ID: " + bike.getId() + ", User ID: " + bike.getUserId());
            assertEquals(5, bike.getUserId(), "Bike должен принадлежать пользователю с ID 5");
        }
    }
}
