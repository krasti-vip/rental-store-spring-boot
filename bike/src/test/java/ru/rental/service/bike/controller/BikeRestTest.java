package ru.rental.service.bike.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.rental.service.bike.BaseBd;
import ru.rental.service.common.dto.BikeDto;
import ru.rental.service.common.dto.BikeDtoCreate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
class BikeRestTest extends BaseBd {

    @Autowired
    private BikeControllerRest bikeControllerRest;

    @Test
    @Description(value = "Тест findById() для мотоцикла")
    @DisplayName("Тест RestController для мотоцикла")
    void findByIdTest() {
        ResponseEntity<BikeDto> response = bikeControllerRest.findById(2);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        BikeDto body = response.getBody();
        assertNotNull(body);
        assertEquals(2, body.getId());
        assertEquals("SUZUKI", body.getName());
        assertEquals(30000, body.getPrice());
        assertEquals(300, body.getHorsePower());
        assertEquals(1.0, body.getVolume());
        assertEquals(1, body.getUserId());
    }

    @Test
    @Description(value = "Тест проверяет получение всех мотоциклов")
    @DisplayName("Тест findAll() для мотоцикла через RestController")
    void findAllTest() {
        ResponseEntity<List<BikeDto>> response = bikeControllerRest.findAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<BikeDto> bikes = response.getBody();
        assertNotNull(bikes);
        assertEquals(5, bikes.size());
        boolean hasBill = bikes.stream().anyMatch(u -> "URAL".equals(u.getName()));
        assertTrue(hasBill, "В списке должен быть мотоцикл URAL");
    }

    @Test
    @Description("Тест проверяет создание мотоцикла через RestController")
    @DisplayName("Тест create() для мотоцикла")
    void createTest() {
        BikeDtoCreate bikeDtoCreate = new BikeDtoCreate();
        bikeDtoCreate.setName("NewBike");
        bikeDtoCreate.setPrice(6000);
        bikeDtoCreate.setHorsePower(45);
        bikeDtoCreate.setVolume(300);
        ResponseEntity<BikeDto> response = bikeControllerRest.create(bikeDtoCreate);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        BikeDto created = response.getBody();
        assertNotNull(created);
        assertEquals("NewBike", created.getName());
        assertEquals(6000, created.getPrice());
        assertEquals(45, created.getHorsePower());
        assertEquals(300, created.getVolume());
        assertTrue(created.getId() > 0);
    }

    @Test
    @Description("Тест проверяет удаление мотоцикла через RestController")
    @DisplayName("Тест delete() для мотоцикла")
    void deleteTest() {
        ResponseEntity<Void> response = bikeControllerRest.delete(5);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        ResponseEntity<BikeDto> afterDelete = bikeControllerRest.findById(5);
        assertEquals(HttpStatus.NOT_FOUND, afterDelete.getStatusCode());
    }

    @Test
    @Description(value = "Тест проверяет обновление данных для мотоцикла через RestController")
    @DisplayName("Тест updateUser() для мотоцикла")
    void updateTest() {
        BikeDto update = new BikeDto();
        update.setId(3);
        update.setName("Aist");
        update.setPrice(1500);
        update.setHorsePower(45);
        update.setUserId(4);
        ResponseEntity<BikeDto> response = bikeControllerRest.update(3, update);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        BikeDto updateBike = response.getBody();
        assertNotNull(updateBike);
        assertEquals("Aist", updateBike.getName());
        assertEquals(1500, updateBike.getPrice());
        assertEquals(1.0, updateBike.getVolume());
        assertEquals(4, updateBike.getUserId());
        assertEquals(3, updateBike.getId());
        assertEquals(45, updateBike.getHorsePower());

    }

    @Test
    @Description(value = "Тест проверяет валидацию мотоцикла при недопустимых значениях через RestController")
    @DisplayName("Тест update() для мотоцикла с неверными данными - ожидание ConstraintViolationException")
    void updateValidationErrorTest() {
        BikeDto update = new BikeDto();
        update.setName("");

        assertThrows(IllegalArgumentException.class, () -> {
            bikeControllerRest.update(1, update);
        });
    }

    @Test
    @Description(value = "Тест проверяет получение мотоцикла пользователя по его id через RestController")
    @DisplayName("Тест getByUserId() для мотоцикла")
    void getByUserIdTest() {
        ResponseEntity<List<BikeDto>> response = bikeControllerRest.getByUserId(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<BikeDto> bike = response.getBody();
        assertNotNull(bike);
        assertEquals(3, bike.size());
        assertEquals("YAMAHA", bike.get(1).getName());
    }
}
