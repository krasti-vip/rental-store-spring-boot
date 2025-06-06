package ru.rental.service.car.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.rental.service.car.BaseBd;
import ru.rental.service.common.dto.CarDto;
import ru.rental.service.common.dto.CarDtoCreate;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
class CarRestTest extends BaseBd {

    @Autowired
    private CarControllerRest carControllerRest;

    @Test
    @Description(value = "Тест findById() для машины")
    @DisplayName("Тест RestController для машины")
    void findByIdTest() {
        ResponseEntity<CarDto> response = carControllerRest.findById(2);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        CarDto body = response.getBody();
        assertNotNull(body);
        assertEquals(2, body.getId());
        assertEquals("HONDA", body.getTitle());
        assertEquals(360.50, body.getPrice());
        assertEquals(190, body.getHorsePower());
        assertEquals(2.4, body.getVolume());
        assertEquals("red", body.getColor());
        assertEquals(1, body.getUserId());
    }

    @Test
    @Description(value = "Тест проверяет получение всех машин")
    @DisplayName("Тест findAll() для машин через RestController")
    void findAllTest() {
        ResponseEntity<List<CarDto>> response = carControllerRest.findAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<CarDto> cars = response.getBody();
        assertNotNull(cars);
        assertEquals(5, cars.size());
        boolean hasBill = cars.stream().anyMatch(u -> "BMW".equals(u.getTitle()));
        assertTrue(hasBill, "В списке должна быть машина BMW");
    }

    @Test
    @Description("Тест проверяет создание машины через RestController")
    @DisplayName("Тест create() для машины")
    void createTest() {
        CarDtoCreate carDtoCreate = new CarDtoCreate();
        carDtoCreate.setUserId(2);
        carDtoCreate.setTitle("NewCar");
        carDtoCreate.setPrice(6000);
        carDtoCreate.setHorsePower(45);
        carDtoCreate.setVolume(30.10);
        carDtoCreate.setColor("red");
        ResponseEntity<CarDto> response = carControllerRest.create(carDtoCreate);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        CarDto created = response.getBody();
        assertNotNull(created);
        assertEquals(2, created.getUserId());
        assertEquals("NewCar", created.getTitle());
        assertEquals(6000, created.getPrice());
        assertEquals(45, created.getHorsePower());
        assertEquals(30.10, created.getVolume());
        assertEquals("red", created.getColor());
        assertTrue(created.getId() > 0);
    }
}
