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

import static org.junit.jupiter.api.Assertions.*;

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
        carDtoCreate.setTitle("NewCar");
        carDtoCreate.setPrice(6000);
        carDtoCreate.setHorsePower(45);
        carDtoCreate.setVolume(30.10);
        carDtoCreate.setColor("red");
        ResponseEntity<CarDto> response = carControllerRest.create(carDtoCreate);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        CarDto created = response.getBody();
        assertNotNull(created);
        assertEquals("NewCar", created.getTitle());
        assertEquals(6000, created.getPrice());
        assertEquals(45, created.getHorsePower());
        assertEquals(30.10, created.getVolume());
        assertEquals("red", created.getColor());
        assertTrue(created.getId() > 0);
    }

    @Test
    @Description("Тест проверяет удаление автомобиля через RestController")
    @DisplayName("Тест delete() для автомобиля")
    void deleteTest() {
        ResponseEntity<Void> response = carControllerRest.delete(5);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        ResponseEntity<CarDto> afterDelete = carControllerRest.findById(5);
        assertEquals(HttpStatus.NOT_FOUND, afterDelete.getStatusCode());
    }

    @Test
    @Description(value = "Тест проверяет обновление данных для автомобиля через RestController")
    @DisplayName("Тест updateUser() для автомобиля")
    void updateTest() {
        CarDto update = new CarDto();
        update.setId(2);
        update.setTitle("Aist");
        update.setPrice(1500);
        update.setHorsePower(45);
        update.setVolume(30.10);
        update.setColor("red");
        update.setUserId(4);
        ResponseEntity<CarDto> response = carControllerRest.update(2, update);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        CarDto updateCar = response.getBody();
        assertNotNull(updateCar);
        assertEquals("Aist", updateCar.getTitle());
        assertEquals(1500, updateCar.getPrice());
        assertEquals(30.10, updateCar.getVolume());
        assertEquals(4, updateCar.getUserId());
        assertEquals(2, updateCar.getId());
        assertEquals(45, updateCar.getHorsePower());

    }

    @Test
    @Description(value = "Тест проверяет валидацию машины при недопустимых значениях через RestController")
    @DisplayName("Тест update() для машины с неверными данными - ожидание ConstraintViolationException")
    void updateValidationErrorTest() {
        CarDto update = new CarDto();
        update.setTitle("");

        assertThrows(IllegalArgumentException.class, () -> {
            carControllerRest.update(1, update);
        });
    }

    @Test
    @Description(value = "Тест проверяет получение машины пользователя по его id через RestController")
    @DisplayName("Тест getByUserId() для машины")
    void getByUserIdTest() {
        ResponseEntity<List<CarDto>> response = carControllerRest.getCarByUserId(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<CarDto> car = response.getBody();
        assertNotNull(car);
        assertEquals(1, car.size());
        assertEquals("MERCEDES", car.get(0).getTitle());
    }
}
