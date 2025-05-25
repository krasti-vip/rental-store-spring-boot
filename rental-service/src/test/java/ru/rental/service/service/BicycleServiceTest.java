package ru.rental.service.service;

import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rental.service.BaseBd;
import ru.rental.service.bicycle.dto.BicycleDto;
import ru.rental.service.bicycle.dto.BicycleDtoCreate;
import ru.rental.service.bicycle.service.BicycleService;
import ru.rental.service.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BicycleServiceTest extends BaseBd {

    @Autowired
    private BicycleService bicycleService;

    @Autowired
    private UserService userService;

    @Test
    @Description(value = "Тест проверяет получение велосипеда по id")
    @DisplayName("Тест findBiId() для bicycle")
    void findBiId() {
        assertTrue(bicycleService.findById(1).isPresent());
        assertEquals("Aist", bicycleService.findById(3).get().getModel());
    }

    @Test
    @Description(value = "Тест проверяет сохранение и удаление велосипеда")
    @DisplayName("Тест create() и delete() для велосипеда")
    void createAndDeleteTest() {

        BicycleDtoCreate bicycleDtoCreate = new BicycleDtoCreate(
                "Kok",
                54.5,
                "black",
                5
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
                4
        );

        assertTrue(bicycleService.findByUserId(3).isEmpty());
        bicycleService.update(bicycleDto);
        assertEquals(4, bicycleService.findById(3).get().getUserId());
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
