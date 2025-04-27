package ru.rental.service.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.rental.service.BaseBd;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BankCardRestTest extends BaseBd {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    private static final String BANK_CARD_URL = "/api/bankcards";

    private static final String JSON_CREATE_CARD = """
            {
                "userId": 1,
                "numberCard": "1234123412341234",
                "expirationDate": "12/25",
                "secretCode": 100
            }
            """;

    @Test
    void createBankCard() throws Exception {
        final var mvcResult = mockMvc.perform(
                post(BANK_CARD_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_CREATE_CARD)
        ).andExpect(status().isCreated()).andReturn();

        final var jsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertDoesNotThrow(() -> jsonNode.get("id").asInt());
        assertEquals(jsonNode.get("numberCard").asText(), "1234123412341234");
        assertEquals(jsonNode.get("expirationDate").asText(), "12/25");
        assertEquals(jsonNode.get("secretCode").asInt(), 100);

        final var getCreatedCard =
                mockMvc.perform(get(BANK_CARD_URL + "/6")).andExpect(status().isOk()).andReturn();

        final var jsonNode2 = mapper.readTree(getCreatedCard.getResponse().getContentAsString());

        assertDoesNotThrow(() -> jsonNode2.get("id").asInt());
        assertEquals(jsonNode2.get("numberCard").asText(), "1234123412341234");
        assertEquals(jsonNode2.get("expirationDate").asText(), "12/25");
        assertEquals(jsonNode2.get("secretCode").asInt(), 100);
    }
}
