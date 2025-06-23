package ru.rental.service.bicycle.service;

import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rental.service.bicycle.BaseBd;
import ru.rental.service.common.dto.BicycleDto;
import ru.rental.service.common.dto.BicycleDtoCreate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BicycleServiceTest extends BaseBd {

    @Autowired
    private BicycleService bicycleService;

    @Test
    @Description(value = "Тест проверяет получение велосипеда по id")
    @DisplayName("Тест findBiId() для bicycle")
    void findBiId() {
        assertTrue(bicycleService.findById(1).isPresent());
        assertEquals("Aist", bicycleService.findById(3).get().getModel());

        Optional<BicycleDto> bicycleDto = bicycleService.findById(1);
        assertEquals(1, bicycleDto.get().getUserId());
    }

    @Test
    @Description(value = "Тест проверяет сохранение и удаление велосипеда")
    @DisplayName("Тест create() и delete() для велосипеда")
    void createAndDeleteTest() {

        BicycleDtoCreate bicycleDtoCreate = new BicycleDtoCreate(
                "Kok",
                54.5,
                "black",
                null
        );
        assertEquals(5, bicycleService.getAll().size());
        bicycleService.create(bicycleDtoCreate);
        assertEquals(6, bicycleService.getAll().size());
        assertEquals("Kok", bicycleService.getAll().get(5).getModel());

        assertTrue(bicycleService.delete(6));
        assertEquals(5, bicycleService.getAll().size());
    }

    @Test
    @Description("Тест проверяет обновление велосипеда")
    @DisplayName("Тест bicycleUpdate() для велосипеда")
    void bicycleUpdateTest() {

        BicycleDto bicycleDto = new BicycleDto(
                3,
                "Kok",
                54.5,
                "black",
                2
        );

        assertTrue(bicycleService.findByUserId(4).isEmpty());
        bicycleService.update(bicycleDto);
    }

    @Test
    @Description(value = "Тест проверяет получение всех велосипедов")
    @DisplayName("Тест getAll() для велосипедов")
    void getAllTest() {
        List<BicycleDto> bicycles = bicycleService.getAll();
        assertFalse(bicycles.isEmpty());
        assertEquals(5, bicycles.size());
    }
}
