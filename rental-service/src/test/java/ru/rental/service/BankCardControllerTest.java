package ru.rental.service;

import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.rental.service.controller.BankCardController;
import ru.rental.service.dto.BankCardDto;
import ru.rental.service.service.BankCardService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Тест BankCardController")
class BankCardControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BankCardService bankCardService;

    @InjectMocks
    private BankCardController bankCardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bankCardController).build();
    }

    @Test
    @Description(value = "Тест проверяет возвращение всех банковских карт на главную страницу")
    @DisplayName("Тест getAll для bankcard/index")
    void getAllBankCardTest() throws Exception {
        when(bankCardService.getAll()).thenReturn(Collections.singletonList(new BankCardDto()));

        mockMvc.perform(get("/bankcard"))
                .andExpect(status().isOk())
                .andExpect(view().name("bankcard/index"))
                .andExpect(model().attributeExists("bankcards"));

        verify(bankCardService, times(1)).getAll();
    }

    @Test
    @Description(value = "Тест проверяет возвращение банковской карты по его айди на новую страницу")
    @DisplayName("Тест get для bankcard/1")
    void getBankCardById_ShouldReturnTest() throws Exception {
        BankCardDto bankCardDto = new BankCardDto();
        bankCardDto.setId(1);
        when(bankCardService.get(1)).thenReturn(Optional.of(bankCardDto));

        mockMvc.perform(get("/bankcard/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bankcard/bankcard"))
                .andExpect(model().attributeExists("bankcard"))
                .andExpect(model().attribute("bankcard", bankCardDto));

        verify(bankCardService, times(1)).get(1);
    }

    @Test
    @Description(value = "Тест проверяет возвращение банковской карты по айди и что он не пустой")
    @DisplayName("Тест get для bankcard/1 no found")
    void getBankCardByIdTest() throws Exception {
        when(bankCardService.get(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/bankcard/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("bankCard no found id: 1"));

        verify(bankCardService, times(1)).get(1);
    }

    @Test
    @Description(value = "Тест проверяет открытия страницы добавления банковской карты bankcard/form")
    @DisplayName("Тест bankcard/create")
    void getCreateBankCardFormTest() throws Exception {
        mockMvc.perform(get("/bankcard/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("bankcard/form"))
                .andExpect(model().attributeExists("bankcard"));
    }

    @Test
    @Description(value = "Тест проверяет сохранение новой банковской карты")
    @DisplayName("Тест сохранения банковской карты")
    void createBankCardTest() throws Exception {
        BankCardDto bankCardDto = new BankCardDto();
        bankCardDto.setId(1);

        mockMvc.perform(post("/bankcard")
                        .flashAttr("bankcard", bankCardDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bankcard"));

        verify(bankCardService, times(1)).save(bankCardDto);
    }

    @Test
    @Description(value = "Тест проверяет удаление банковской карты")
    @DisplayName("Тест удаление банковской карты")
    void deleteBankCardByIdTest() throws Exception {
        mockMvc.perform(delete("/bankcard/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bankcard"));

        verify(bankCardService, times(1)).delete(1);
    }

    @Test
    @Description(value = "Тест проверяет сохранение обновления банковской карты")
    @DisplayName("Тест обновления банковской карты")
    void updateBankCardByIdTest() throws Exception {
        BankCardDto bankCardDto = new BankCardDto();
        bankCardDto.setId(1);

        mockMvc.perform(patch("/bankcard/1")
                        .flashAttr("bankcard", bankCardDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bankcard"));

        verify(bankCardService, times(1)).update(1, bankCardDto);
    }

    @Test
    @Description(value = "Тест проверяет открытия страницы bankcard/update выбранной банковской карты")
    @DisplayName("Тест bankcard/update")
    void showUpdateFormTest() throws Exception {
        BankCardDto bankCardDto = new BankCardDto();
        bankCardDto.setId(1);
        when(bankCardService.get(1)).thenReturn(Optional.of(bankCardDto));

        mockMvc.perform(get("/bankcard/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("bankcard/update"))
                .andExpect(model().attributeExists("bankcard"))
                .andExpect(model().attribute("bankcard", bankCardDto));

        verify(bankCardService, times(1)).get(1);
    }
}
