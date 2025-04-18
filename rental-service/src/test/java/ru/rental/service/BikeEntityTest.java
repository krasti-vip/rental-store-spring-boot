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
import ru.rental.service.repository.BikeRepository;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Тест BikeDao")
class BikeEntityTest extends BaseBd {

    @Autowired
    private BikeRepository bikeRepository;

    private static Stream<Arguments> sourceBike() {
        return Stream.of(
                Arguments.of(
                        Bike.builder()
                                .id(1)
                                .name("BMW")
                                .price(2000.)
                                .horsePower(200)
                                .volume(1.0)
                                .build()
                ),
                Arguments.of(
                        Bike.builder()
                                .id(2)
                                .name("SUZUKI")
                                .price(30000.)
                                .horsePower(300)
                                .volume(1.0)
                                .build()
                ),
                Arguments.of(
                        Bike.builder()
                                .id(3)
                                .name("YAMAHA")
                                .price(40000.)
                                .horsePower(400)
                                .volume(1.0)
                                .build()
                ),
                Arguments.of(
                        Bike.builder()
                                .id(4)
                                .name("URAL")
                                .price(2000.)
                                .horsePower(200)
                                .volume(1.0)
                                .build()
                ),
                Arguments.of(
                        Bike.builder()
                                .id(5)
                                .name("HONDA")
                                .price(2000.)
                                .horsePower(200)
                                .volume(1.0)
                                .build()
                )
        );
    }

    @Test
    @Description(value = "Тест проверяет возвращения всех мотоциклов")
    @DisplayName("Test getAllBike")
    void bikeDaoGetAll() {
        List<Bike> allBikes = (List<Bike>) bikeRepository.findAll();
        assertEquals(5, allBikes.size());
    }

    @ParameterizedTest
    @MethodSource("sourceBike")
    @Description(value = "Тест проверяет возвращения байков и что у них правильные поля")
    @DisplayName("Test getBike")
    void getBikeTest(Bike sourceBike) {
        final var bike = bikeRepository.findById(sourceBike.getId());

        assertAll(
                () -> assertEquals(bike.get().getId(), sourceBike.getId()),
                () -> assertEquals(bike.get().getName(), sourceBike.getName()),
                () -> assertEquals(bike.get().getPrice(), sourceBike.getPrice()),
                () -> assertEquals(bike.get().getVolume(), sourceBike.getVolume()),
                () -> assertEquals(bike.get().getHorsePower(), sourceBike.getHorsePower())
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
                sourceBike.getUser()
        );

        final var updatedBikeBd = bikeRepository.save(sourceBike);
        final var bikeUpdate = bikeRepository.save(updatedBikeBd);

        assertAll(
                () -> assertEquals(bikeUpdate.getId(), updatedBike.getId()),
                () -> assertEquals(bikeUpdate.getName(), updatedBike.getName()),
                () -> assertEquals(bikeUpdate.getPrice(), updatedBike.getPrice()),
                () -> assertEquals(bikeUpdate.getHorsePower(), updatedBike.getHorsePower()),
                () -> assertEquals(bikeUpdate.getVolume(), updatedBike.getVolume())
        );
    }

    @ParameterizedTest
    @MethodSource("sourceBike")
    @Description(value = "Тест проверяет сохранение мотоцикла, а затем его удаление и что количество мотоциклов сначала " +
                         "изменилось, а потом вернулась")
    @DisplayName("Тест сохранения и удаления мотоцикла")
    void saveAndDeleteBikeTest(Bike sourceBike) {
        Bike bikeToSave = new Bike(
                999,
                sourceBike.getName(),
                sourceBike.getPrice(),
                sourceBike.getHorsePower(),
                sourceBike.getVolume(),
                sourceBike.getUser()
        );

        Bike savedBike = bikeRepository.save(bikeToSave);
        assertNotNull(savedBike, "Байк должен быть успешно сохранен");

        List<Bike> allBikes = (List<Bike>) bikeRepository.findAll();
        assertEquals(6, allBikes.size(), "После сохранения в базе должно быть 6 байков");
        assertTrue(bikeRepository.existsById(savedBike.getId()), "Сохраненный байк должен быть в списке");

        assertAll(
                () -> assertEquals(savedBike.getName(), sourceBike.getName(), "Имя байка должно совпадать"),
                () -> assertEquals(savedBike.getPrice(), sourceBike.getPrice(), "Цена байка должна совпадать"),
                () -> assertEquals(savedBike.getHorsePower(), sourceBike.getHorsePower(), "Мощность байка должна совпадать"),
                () -> assertEquals(savedBike.getVolume(), sourceBike.getVolume(), "Объем байка должен совпадать")
        );

        bikeRepository.deleteById(savedBike.getId());

        assertFalse(bikeRepository.existsById(savedBike.getId()), "Попытка получить удаленный байк должна вернуть false");
        List<Bike> allBikes2 = (List<Bike>) bikeRepository.findAll();
        assertEquals(5, allBikes2.size(), "После удаления байка в базе должно быть 5 байков");
    }
}
