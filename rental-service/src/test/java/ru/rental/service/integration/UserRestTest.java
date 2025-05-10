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
class UserRestTest extends BaseBd {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    private static final String USER_URL = "/api/user";

    private static final String JSON_CREATE_USER = """
            {
                "userName": "Igr",
                "firstName": "Ugor",
                "lastName": "Zepnov",
                "passport": 1234567890,
                "email": null,
                "bikes": null,
                "cars": null,
                "bicycles": null
            }
            """;

    @Test
    @DisplayName("Тест создания пользователя")
    void createCar() throws Exception {
        final var mvcResult = mockMvc.perform(
                post(USER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_CREATE_USER)
        ).andExpect(status().isCreated()).andReturn();

        final var jsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertDoesNotThrow(() -> jsonNode.get("id").asInt());
        assertEquals(jsonNode.get("userName").asText(), "Igr");
        assertEquals(jsonNode.get("firstName").asText(), "Ugor");
        assertEquals(jsonNode.get("lastName").asText(), "Zepnov");
        assertEquals(jsonNode.get("passport").asInt(), 1234567890);
        assertTrue(jsonNode.get("email").isNull());
        assertTrue(jsonNode.get("bikes").isNull());
        assertTrue(jsonNode.get("cars").isNull());
        assertTrue(jsonNode.get("bicycles").isNull());

        final var getCreateUser =
                mockMvc.perform(get(USER_URL + "/6")).andExpect(status().isOk()).andReturn();

        final var jsonNode2 = mapper.readTree(getCreateUser.getResponse().getContentAsString());

        assertEquals(jsonNode2.get("userName").asText(), "Igr");
        assertEquals(jsonNode2.get("firstName").asText(), "Ugor");
        assertEquals(jsonNode2.get("lastName").asText(), "Zepnov");
        assertEquals(jsonNode2.get("passport").asInt(), 1234567890);
        assertTrue(jsonNode2.get("email").isNull());
        assertTrue(jsonNode2.get("bikes").size() == 0);
        assertTrue(jsonNode2.get("cars").size() == 0);
        assertTrue(jsonNode2.get("bicycles").size() == 0);
    }
}
