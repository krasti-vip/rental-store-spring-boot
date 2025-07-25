package ru.rental.service.bicycle.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rental.service.bicycle.BaseBd;
import ru.rental.service.bicycle.entity.Bicycle;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BicycleRepositoryTest extends BaseBd {

    @Autowired
    private BicycleRepository bicycleRepository;

    @Test
    @DisplayName("Тест получения всех велосипедов")
    void findAllTest() {
        List<Bicycle> bicycles = (List<Bicycle>) bicycleRepository.findAll();
        assertEquals(5, bicycles.size());
    }

    @Test
    @Transactional
    @DisplayName("Тест сохранения и удаления нового велосипеда")
    void saveAndDeleteTest() {
        Bicycle bicycle = new Bicycle(
                null,
                "Mongust",
                123.56,
                "Black",
                3
        );
        bicycleRepository.save(bicycle);
        List<Bicycle> bicycles = (List<Bicycle>) bicycleRepository.findAll();
        assertEquals(6, bicycles.size());

        bicycleRepository.deleteById(6);
        bicycles = (List<Bicycle>) bicycleRepository.findAll();
        assertEquals(5, bicycles.size());
    }

    @Test
    @DisplayName("Тест возвращения велосипеда по id")
    void findByIdTest() {
        Bicycle bike = bicycleRepository.findById(3).orElseThrow();
        assertEquals(1500., bike.getPrice());
    }

    @Test
    @DisplayName("Тест обновление велосипеда")
    void updateBicycleTest() {
        Bicycle bike = bicycleRepository.findById(1).orElseThrow();
        bike.setPrice(3456.56);
        bicycleRepository.save(bike);
        assertEquals(bike, bicycleRepository.findById(1).get());
    }

    @Test
    @DisplayName("Тест получение карт User")
    void findBicycleByUserTest() {
        List<Bicycle> bike = bicycleRepository.findByUserId(1);
        assertEquals(2, bike.size());
    }
}
