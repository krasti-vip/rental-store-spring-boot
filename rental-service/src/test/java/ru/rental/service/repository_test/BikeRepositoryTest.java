package ru.rental.service.repository_test;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rental.service.BaseBd;
import ru.rental.service.entity.Bike;
import ru.rental.service.repository.BikeRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BikeRepositoryTest extends BaseBd {

    @Autowired
    private BikeRepository bikeRepository;

    @Test
    @DisplayName("Тест возвращения всех мотоциклов")
    void findAllBikeTest() {
        List<Bike> bikes = (List<Bike>) bikeRepository.findAll();
        assertEquals(5, bikes.size());
    }

    @Test
    @Transactional
    @DisplayName("Тест сохранения и удаление нового мотоцикла")
    void saveAndDeleteBikeTest() {
        Bike bike = new Bike(
                6,
                "Suzuki",
                345.7,
                54,
                1.0,
                null
        );

        bikeRepository.save(bike);
        List<Bike> bikes = (List<Bike>) bikeRepository.findAll();
        assertEquals(6, bikes.size());
        bikeRepository.deleteById(6);
        List<Bike> bikeNew = (List<Bike>) bikeRepository.findAll();
        assertEquals(5, bikeNew.size());
    }

    @Test
    @DisplayName("Тест возвращение мотоцикла по id")
    void findBikeByIdTest() {
        Optional<Bike> bike = bikeRepository.findById(1);
        assertTrue(bike.isPresent());
        assertEquals("BMW", bike.get().getName());
    }

    @Test
    @DisplayName("Тест обновление мотоцикла")
    void updateBikeTest() {
        Bike bike = bikeRepository.findById(1).orElseThrow();
        bike.setPrice(888888.3);
        bikeRepository.save(bike);
        assertEquals(bike, bikeRepository.findById(1).get());
    }

    @Test
    @DisplayName("Тест получение мотоциклов User")
    void findBikeByUserTest() {
        List<Bike> bikes = bikeRepository.findByUserId(1);
        assertEquals(1, bikes.size());
    }
}
