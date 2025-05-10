package ru.rental.service.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.rental.service.BaseBd;


@SpringBootTest
@AutoConfigureMockMvc
class BicycleRestTest extends BaseBd {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    private static final String BICYCLE_URL = "/api/bicycle";

    private static final String JSON_CREATE_BICYCLE = """
            {
                "model": "Mongust",
                "price": 12.95,
                "color": "red",
                "userId": 1
            }
            """;

    @Test
    @DisplayName("Тест создания велосипеда")
    void createBicycle() throws Exception {
    }
}
