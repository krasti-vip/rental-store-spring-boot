package ru.rental.service.rental.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.rental.service.common.dto.RentalDto;
import ru.rental.service.common.dto.RentalDtoCreate;
import ru.rental.service.rental.BaseBd;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
class RentalRestTest extends BaseBd {

    @Autowired
    private RentalControllerRest rentalControllerRest;

    @Test
    @Description(value = "Тест findById() для аренды")
    @DisplayName("Тест RestController для аренды")
    void findByIdTest() {
        ResponseEntity<RentalDto> response = rentalControllerRest.findById(2);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RentalDto body = response.getBody();
        assertNotNull(body);
        assertEquals(2, body.getId());
        assertEquals(1, body.getUserId());
        assertEquals(1, body.getCarId());
        assertNull(body.getBikeId());
        assertNull(body.getBicycleId());
        assertEquals("2025-03-05T14:00", body.getStartDate().toString());
        assertEquals("2025-03-10T09:00", body.getEndDate().toString());
        assertEquals(4500.50, body.getRentalAmount());
        assertFalse(body.getIsPaid());
    }

    @Test
    @Description(value = "Тест проверяет получение всех аренд")
    @DisplayName("Тест findAll() для аренд через RestController")
    void findAllTest() {
        ResponseEntity<List<RentalDto>> response = rentalControllerRest.findAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<RentalDto> rentals = response.getBody();
        assertNotNull(rentals);
        assertEquals(4, rentals.size());
        boolean hasBill = rentals.stream().anyMatch(u -> 12000.00 == (u.getRentalAmount()));
        assertTrue(hasBill, "В списке должна быть аренда на 12000.00");
    }

    @Test
    @Description("Тест проверяет создание аренды через RestController")
    @DisplayName("Тест create() для аренды")
    void createTest() {
        RentalDtoCreate rentalDtoCreate = new RentalDtoCreate();
        rentalDtoCreate.setUserId(2);
        rentalDtoCreate.setCarId(1);
        rentalDtoCreate.setBikeId(2);
        rentalDtoCreate.setBicycleId(3);
        rentalDtoCreate.setStartDate(LocalDateTime.parse("2025-03-05T14:00"));
        rentalDtoCreate.setEndDate(null);
        rentalDtoCreate.setRentalAmount(4500.50);
        rentalDtoCreate.setIsPaid(false);
        //ResponseEntity<RentalDto> response = rentalControllerRest.create(rentalDtoCreate);
        //assertEquals(HttpStatus.CREATED, response.getStatusCode());
        //RentalDto created = response.getBody();
//        assertNotNull(created);
//        assertEquals(2, created.getUserId());
//        assertEquals(1, created.getCarId());
//        assertEquals(2, created.getBikeId());
//        assertEquals(3, created.getBicycleId());
//        assertEquals(LocalDateTime.parse("2025-03-05T14:00"), created.getStartDate());
//        assertNull(created.getEndDate());
//        assertEquals(4500.50, created.getRentalAmount());
//        assertFalse(created.getIsPaid());
    }
}
