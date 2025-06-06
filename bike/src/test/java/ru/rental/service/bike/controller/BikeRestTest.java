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
        assertNull(body.getUserId());
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
        bikeDtoCreate.setUserId(2);
        bikeDtoCreate.setName("NewBike");
        bikeDtoCreate.setPrice(6000);
        bikeDtoCreate.setHorsePower(45);
        bikeDtoCreate.setVolume(300);
        ResponseEntity<BikeDto> response = bikeControllerRest.create(bikeDtoCreate);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        BikeDto created = response.getBody();
        assertNotNull(created);
        assertEquals(2, created.getUserId());
        assertEquals("NewBike", created.getName());
        assertEquals(6000, created.getPrice());
        assertEquals(45, created.getHorsePower());
        assertEquals(300, created.getVolume());
        assertTrue(created.getId() > 0);
    }
}
