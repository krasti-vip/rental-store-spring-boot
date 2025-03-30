package ru.rental.service;


import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rental.service.dao.BicycleDao;
import ru.rental.service.dao.UserDao;
import ru.rental.service.model.Bicycle;
import ru.rental.service.model.User;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Тест BicycleDao")
class BicycleDaoTest extends BaseBd {

    @Autowired
    private BicycleDao bicycleDao;

    private static Stream<Arguments> sourceBicycle() {
        return Stream.of(
                Arguments.of(
                        Bicycle.builder()
                                .id(1)
                                .model("Mongust")
                                .price(2000)
                                .color("blue")
                                .build()
                ),
                Arguments.of(
                        Bicycle.builder()
                                .id(2)
                                .model("Ctels")
                                .price(1000)
                                .color("black")
                                .build()
                ),
                Arguments.of(
                        Bicycle.builder()
                                .id(3)
                                .model("Aist")
                                .price(1500)
                                .color("white")
                                .build()
                ),
                Arguments.of(
                        Bicycle.builder()
                                .id(4)
                                .model("BMX")
                                .price(2500)
                                .color("blue")
                                .build()
                ),
                Arguments.of(
                        Bicycle.builder()
                                .id(5)
                                .model("Mochina")
                                .price(1200)
                                .color("red")
                                .build()
                )
        );
    }

    private static Stream<Arguments> sourceBicycleForFilterTest() {
        return Stream.of(
                Arguments.of(new Bicycle(1, "Mongust", 2000, "blue", null), "Mongust", "Mongust"),
                Arguments.of(new Bicycle(2, "Ctels", 1000, "black", null), "Ctels", "Ctels"),
                Arguments.of(new Bicycle(3, "Aist", 1500, "white", null), "Aist", "Aist"),
                Arguments.of(new Bicycle(4, "BMX", 2500, "blue", null), "BMX", "BMX"),
                Arguments.of(new Bicycle(5, "Mochina", 1200, "red", null), "Mochina", "Mochina")
        );
    }

    @Test
    @Description(value = "Тест проверяет возвращения всех велосипедов")
    @DisplayName("Test getAllBicycle")
    void bicycleDaoGetAll() {
        final var allBicycle = bicycleDao.getAll();
        assertEquals(5, allBicycle.size());
    }

    @Test
    @Description(value = "Тест проверяет была ли создана таблица bicycles")
    @DisplayName("Test создания Table bicycles")
    void createTableTest() {
        bicycleDao.createTable();
        boolean creatTrue = bicycleDao.checkIfTableExists("bicycles");
        assertTrue(creatTrue);
        boolean noCreat = bicycleDao.checkIfTableExists("test");
        assertFalse(noCreat);
    }

    @ParameterizedTest
    @MethodSource("sourceBicycle")
    @Description(value = "Тест проверяет возвращения велосипедов и что у них правильные поля")
    @DisplayName("Test getBicycle")
    void getBicycleTest(Bicycle sourceBicycle) {
        final var bicycle = bicycleDao.get(sourceBicycle.getId());

        assertAll(
                () -> assertEquals(bicycle.getId(), sourceBicycle.getId()),
                () -> assertEquals(bicycle.getModel(), sourceBicycle.getModel()),
                () -> assertEquals(bicycle.getPrice(), sourceBicycle.getPrice()),
                () -> assertEquals(bicycle.getColor(), sourceBicycle.getColor())
        );
    }

    @ParameterizedTest
    @MethodSource("sourceBicycle")
    @Description(value = "Тест проверяет обновление велосипедов, а так же все их поля на соответствие изменений")
    @DisplayName("Тест обновления велосипедов")
    void updateBicycleTest(Bicycle sourceBicycle) {

        Bicycle updatedBicycle = new Bicycle(
                sourceBicycle.getId(),
                sourceBicycle.getModel(),
                sourceBicycle.getPrice() + 1000,
                "red",
                sourceBicycle.getUserId()
        );

        Bicycle nonBicycle = new Bicycle(
                -1,
                sourceBicycle.getModel(),
                sourceBicycle.getPrice() + 1000,
                "red",
                sourceBicycle.getUserId()
        );

        int nonBicycleId = nonBicycle.getId();

        final var updatedBicycleBd = bicycleDao.update(sourceBicycle.getId(), updatedBicycle);
        final var bicycleUpdate = bicycleDao.get(updatedBicycleBd.getId());

        assertAll(
                () -> assertEquals(bicycleUpdate.getId(), updatedBicycle.getId()),
                () -> assertEquals(bicycleUpdate.getModel(), updatedBicycle.getModel()),
                () -> assertEquals(bicycleUpdate.getPrice(), updatedBicycle.getPrice()),
                () -> assertEquals(bicycleUpdate.getColor(), updatedBicycle.getColor()),
                () -> assertThrows(IllegalStateException.class, () -> {
                    bicycleDao.update(nonBicycleId, updatedBicycle);
                }, "Expected update to throw an exception as the bicycle does not exist")
        );
    }

    @ParameterizedTest
    @MethodSource("sourceBicycle")
    @Description(value = "Тест проверяет сохранение велосипеда, а затем его удаление и что количество велосипедов сначала " +
                         "изменилось, а потом вернулась")
    @DisplayName("Тест сохранения и удаления велосипеда")
    void saveAndDeleteBicycleTest(Bicycle sourceBicycle) {
        Bicycle bicycleToSave = new Bicycle(
                -896,
                sourceBicycle.getModel(),
                sourceBicycle.getPrice(),
                sourceBicycle.getColor(),
                sourceBicycle.getUserId()
        );

        Bicycle savedBicycle = bicycleDao.save(bicycleToSave);
        assertNotNull(savedBicycle, "Велосипед должен быть успешно сохранен");

        assertEquals(6, bicycleDao.getAll().size(), "После сохранения в базе должно быть 6 велосипедов");
        assertTrue(bicycleDao.getAll().stream().anyMatch(b -> b.getId() == savedBicycle.getId()), "Сохраненный Bелосипед должен быть в списке");

        Bicycle savedBicycleFromDb = bicycleDao.get(savedBicycle.getId());
        assertAll(
                () -> assertEquals(bicycleToSave.getModel(), savedBicycleFromDb.getModel(), "Имя велосипеда должно совпадать"),
                () -> assertEquals(bicycleToSave.getPrice(), savedBicycleFromDb.getPrice(), "Цена велосипеда должна совпадать"),
                () -> assertEquals(bicycleToSave.getColor(), savedBicycleFromDb.getColor(), "Цвет велосипеда должен совпадать")
        );

        assertTrue(bicycleDao.delete(savedBicycle.getId()), "Велосипед должен быть успешно удален");
        assertFalse(bicycleDao.delete(999), "Попытка удаления несуществующего велосипеда должна вернуть false");
        assertEquals(5, bicycleDao.getAll().size(), "После удаления велосипеда в базе должно быть 5 велосипедов");
    }

    @ParameterizedTest
    @MethodSource("sourceBicycleForFilterTest")
    @Description(value = "Тест проверяет фильтрацию базы велосипедов по предикату")
    @DisplayName("Тест фильтрации велосипедов")
    void filtrBicycleTest(Bicycle sourceBicycleForFilterTest, String filterKeyword, String expectedName) {

        Predicate<Bicycle> predicate = a -> a.getModel().contains(filterKeyword);

        List<Bicycle> filteredBicycle = bicycleDao.filterBy(predicate);

        assertTrue(filteredBicycle.stream().anyMatch(b -> b.getModel().equals(expectedName)),
                "Отфильтрованный велосипед должен иметь модель " + expectedName);
    }

    @Test
    @Description(value = "Тест проверяет обновление списка велосипедов у пользователя, а так же что данный велосипед принадлежит" +
                         "определенному пользователю")
    @DisplayName("Тест добавление аренды велосипеда")
    void updateUserId() {
        UserDao userDao = new UserDao();
        bicycleDao.updateUserId(2, 5);
        User user = userDao.get(5);
        assertNotNull(user, "Пользователь с ID 5 не найден");
        List<Bicycle> userBicycles = bicycleDao.getAllByUserId(user.getId());
        assertNotNull(userBicycles, "Список велосипедов у пользователя не должен быть null");
        assertFalse(userBicycles.isEmpty(), "Список велосипедов у пользователя не должен быть пустым");
        for (Bicycle bicycle : userBicycles) {
            System.out.println("Bike ID: " + bicycle.getId() + ", User ID: " + bicycle.getUserId());
            assertEquals(5, bicycle.getUserId(), "Велосипед должен принадлежать пользователю с ID 5");
        }
    }
}
