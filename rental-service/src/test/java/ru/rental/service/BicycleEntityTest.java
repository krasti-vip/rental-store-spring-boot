package ru.rental.service;


import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rental.service.entity.Bicycle;
import ru.rental.service.repository.BicycleRepository;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Тест BicycleDao")
class BicycleEntityTest extends BaseBd {

    @Autowired
    private BicycleRepository bicycleRepository;

    private static Stream<Arguments> sourceBicycle() {
        return Stream.of(
                Arguments.of(
                        Bicycle.builder()
                                .id(1)
                                .model("Mongust")
                                .price(2000.)
                                .color("blue")
                                .build()
                ),
                Arguments.of(
                        Bicycle.builder()
                                .id(2)
                                .model("Ctels")
                                .price(1000.)
                                .color("black")
                                .build()
                ),
                Arguments.of(
                        Bicycle.builder()
                                .id(3)
                                .model("Aist")
                                .price(1500.)
                                .color("white")
                                .build()
                ),
                Arguments.of(
                        Bicycle.builder()
                                .id(4)
                                .model("BMX")
                                .price(2500.)
                                .color("blue")
                                .build()
                ),
                Arguments.of(
                        Bicycle.builder()
                                .id(5)
                                .model("Mochina")
                                .price(1200.)
                                .color("red")
                                .build()
                )
        );
    }

    private static Stream<Arguments> sourceBicycleForFilterTest() {
        return Stream.of(
                Arguments.of(new Bicycle(1, "Mongust", 2000., "blue", null), "Mongust", "Mongust"),
                Arguments.of(new Bicycle(2, "Ctels", 1000., "black", null), "Ctels", "Ctels"),
                Arguments.of(new Bicycle(3, "Aist", 1500., "white", null), "Aist", "Aist"),
                Arguments.of(new Bicycle(4, "BMX", 2500., "blue", null), "BMX", "BMX"),
                Arguments.of(new Bicycle(5, "Mochina", 1200., "red", null), "Mochina", "Mochina")
        );
    }

    @Test
    @Description(value = "Тест проверяет возвращения всех велосипедов")
    @DisplayName("Test getAllBicycle")
    void bicycleDaoGetAllTest() {
        List<Bicycle> allBicycle = (List<Bicycle>) bicycleRepository.findAll();
        assertEquals(5, allBicycle.size());
    }

    @ParameterizedTest
    @MethodSource("sourceBicycle")
    @Description(value = "Тест проверяет возвращения велосипедов и что у них правильные поля")
    @DisplayName("Test getBicycle")
    void getBicycleTest(Bicycle sourceBicycle) {
        final var bicycle = bicycleRepository.findById(sourceBicycle.getId());

        assertAll(
                () -> assertEquals(bicycle.get().getId(), sourceBicycle.getId()),
                () -> assertEquals(bicycle.get().getModel(), sourceBicycle.getModel()),
                () -> assertEquals(bicycle.get().getPrice(), sourceBicycle.getPrice()),
                () -> assertEquals(bicycle.get().getColor(), sourceBicycle.getColor())
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
                sourceBicycle.getUser()
        );

        Bicycle updatedBicycleBd = bicycleRepository.save(updatedBicycle);

        assertAll(
                () -> assertEquals(sourceBicycle.getId(), updatedBicycle.getId()),
                () -> assertEquals(sourceBicycle.getModel(), updatedBicycle.getModel()),
                () -> assertEquals(sourceBicycle.getPrice(), updatedBicycle.getPrice()),
                () -> assertEquals(sourceBicycle.getColor(), updatedBicycle.getColor())
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
                sourceBicycle.getUser()
        );

        Bicycle savedBicycle = bicycleRepository.save(bicycleToSave);
        assertNotNull(savedBicycle, "Велосипед должен быть успешно сохранен");

        List<Bicycle> allBicycle = (List<Bicycle>) bicycleRepository.findAll();
        assertEquals(6, allBicycle.size(), "После сохранения в базе должно быть 6 велосипедов");
        assertTrue(bicycleRepository.existsById(savedBicycle.getId()), "Сохраненный велосипед должен быть в списке");

        Bicycle savedBicycleFromDb = bicycleRepository.findById(savedBicycle.getId())
                .orElseThrow(() -> new RuntimeException("<UNK> <UNK> <UNK> <UNK> <UNK>"));
        assertAll(
                () -> assertEquals(bicycleToSave.getModel(), savedBicycleFromDb.getModel(), "Имя велосипеда должно совпадать"),
                () -> assertEquals(bicycleToSave.getPrice(), savedBicycleFromDb.getPrice(), "Цена велосипеда должна совпадать"),
                () -> assertEquals(bicycleToSave.getColor(), savedBicycleFromDb.getColor(), "Цвет велосипеда должен совпадать")
        );

        bicycleRepository.deleteById(savedBicycle.getId());
        List<Bicycle> allBicycleFromDb = (List<Bicycle>) bicycleRepository.findAll();
        assertEquals(5, allBicycleFromDb.size(), "После удаления велосипеда в базе должно быть 5 велосипедов");
    }
}
