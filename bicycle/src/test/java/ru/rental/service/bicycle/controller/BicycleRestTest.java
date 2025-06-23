package ru.rental.service.bicycle.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.rental.service.bicycle.BaseBd;
import ru.rental.service.common.dto.BicycleDto;
import ru.rental.service.common.dto.BicycleDtoCreate;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
class BicycleRestTest extends BaseBd {

    @Autowired
    private BicycleControllerRest bicycleControllerRest;

    @Test
    @Description(value = "Тест findById() для велосипеда")
    @DisplayName("Тест RestController для велосипеда")
    void findByIdTest() {
        ResponseEntity<BicycleDto> response = bicycleControllerRest.findById(2);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        BicycleDto body = response.getBody();
        assertNotNull(body);
        assertEquals(2, body.getId());
        assertEquals("Ctels", body.getModel());
        assertEquals(1000, body.getPrice());
        assertEquals("black", body.getColor());
    }

    @Test
    @Description(value = "Тест проверяет получение всех велосипедов")
    @DisplayName("Тест findAll() для велосипеда через RestController")
    void findAllTest() {
        ResponseEntity<List<BicycleDto>> response = bicycleControllerRest.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<BicycleDto> bicycles = response.getBody();
        assertNotNull(bicycles);
        assertEquals(5, bicycles.size());
        boolean hasBill = bicycles.stream().anyMatch(u -> "BMX".equals(u.getModel()));
        assertTrue(hasBill, "В списке должен быть велосипед BMX");
    }

    @Test
    @Description("Тест проверяет создание велосипеда через RestController")
    @DisplayName("Тест create() для велосипеда")
    void createTest() {
        BicycleDtoCreate bicycleDtoCreate = new BicycleDtoCreate();
        bicycleDtoCreate.setModel("NewBicycle");
        bicycleDtoCreate.setPrice(6000);
        bicycleDtoCreate.setColor("red");
        ResponseEntity<BicycleDto> response = bicycleControllerRest.create(bicycleDtoCreate);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        BicycleDto created = response.getBody();
        assertNotNull(created);
        assertEquals("NewBicycle", created.getModel());
        assertEquals(6000, created.getPrice());
        assertEquals("red", created.getColor());
        assertTrue(created.getId() > 0);
    }

    @Test
    @Description("Тест проверяет удаление велосипеда через RestController")
    @DisplayName("Тест delete() для велосипеда")
    void deleteTest() {
        ResponseEntity<Void> response = bicycleControllerRest.delete(5);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        ResponseEntity<BicycleDto> afterDelete = bicycleControllerRest.findById(5);
        assertEquals(HttpStatus.NOT_FOUND, afterDelete.getStatusCode());
    }

    @Test
    @Description(value = "Тест проверяет обновление данных для велосипеда через RestController")
    @DisplayName("Тест updateUser() для велосипеда")
    void updateTest() {
        BicycleDto update = new BicycleDto();
        update.setId(3);
        update.setModel("Aist");
        update.setPrice(1500);
        update.setColor("white");
        ResponseEntity<BicycleDto> response = bicycleControllerRest.update(3, update);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        BicycleDto updatedBicycle = response.getBody();
        assertNotNull(updatedBicycle);
        assertEquals("Aist", updatedBicycle.getModel());
        assertEquals(1500, updatedBicycle.getPrice());
        assertEquals("white", updatedBicycle.getColor());
    }

    @Test
    @Description(value = "Тест проверяет валидацию велосипеда при недопустимых значениях через RestController")
    @DisplayName("Тест update() для велосипеда с неверными данными - ожидание ConstraintViolationException")
    void updateValidationErrorTest() {
        BicycleDto update = new BicycleDto();
        update.setModel("");

        assertThrows(IllegalArgumentException.class, () -> {
            bicycleControllerRest.update(1, update);
        });
    }

    @Test
    @Description(value = "Тест проверяет получение велосипеда пользователя по его id через RestController")
    @DisplayName("Тест getByUserId() для велосипеда")
    void getByUserIdTest() {
        ResponseEntity<List<BicycleDto>> response = bicycleControllerRest.getByUserId(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<BicycleDto> bicycles = response.getBody();
        assertNotNull(bicycles);
        assertEquals(2, bicycles.size());
        assertEquals("Ctels", bicycles.get(1).getModel());
    }
}
