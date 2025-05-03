package ru.rental.service.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.rental.service.BaseBd;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RentalRestTest extends BaseBd {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    private static final String RENTAL_URL = "/api/rental";

    private static final String JSON_CREATE_RENTAL = """
            {
                "userId": 1,
                "carId": 1,
                "bikeId": null,
                "bicycleId": null,
                "startDate": "2025-03-01T10:00:00",
                "endDate": null,
                "rentalAmount": 115.95,
                "isPaid": true
            }
            """;

    @Test
    @DisplayName("Тест создания аренды")
    void createCar() throws Exception {

    }
}
