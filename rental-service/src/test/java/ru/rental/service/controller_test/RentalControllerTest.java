package ru.rental.service.controller_test;

import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.rental.service.controller.RentalController;
import ru.rental.service.dto.RentalDto;
import ru.rental.service.service.RentalService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Тест RentalController")
class RentalControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RentalService rentalService;

    @InjectMocks
    private RentalController rentalController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(rentalController).build();
    }

    @Test
    @Description(value = "Тест проверяет возвращение всей аренды на главную страницу")
    @DisplayName("Тест getAll для rental/index")
    void getAllRentalTest() throws Exception {
        when(rentalService.findAll()).thenReturn(Collections.singletonList(new RentalDto()));

        mockMvc.perform(get("/rental"))
                .andExpect(status().isOk())
                .andExpect(view().name("rental/index"))
                .andExpect(model().attributeExists("rentals"));

        verify(rentalService, times(1)).findAll();
    }

    @Test
    @Description(value = "Тест проверяет возвращение аренды по ее айди на новую страницу")
    @DisplayName("Тест get для rental/1")
    void getRentalByIdTest() throws Exception {
        RentalDto rental = new RentalDto();
        rental.setId(1);
        when(rentalService.findById(1)).thenReturn(Optional.of(rental));

        mockMvc.perform(get("/rental/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("rental/rental"))
                .andExpect(model().attributeExists("rental"))
                .andExpect(model().attribute("rental", rental));

        verify(rentalService, times(1)).findById(1);
    }

    @Test
    @Description(value = "Тест проверяет возвращение аренды по айди и что она не пустая")
    @DisplayName("Тест get для rental/1 no found")
    void getRentalById_NotFoundTest() throws Exception {
        when(rentalService.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/rental/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("rental no found id: 1"));

        verify(rentalService, times(1)).findById(1);
    }

    @Test
    @Description(value = "Тест проверяет открытия страницы добавления машины rental/form")
    @DisplayName("Тест rental/create")
    void getCreateRentalFormTest() throws Exception {
        mockMvc.perform(get("/rental/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("rental/form"))
                .andExpect(model().attributeExists("rental"));
    }

    @Test
    @Description(value = "Тест проверяет сохранение новой аренды")
    @DisplayName("Тест сохранения аренды")
    void createRentalTest() throws Exception {
        RentalDto rentalDto = new RentalDto();
        rentalDto.setId(1);

        mockMvc.perform(post("/rental")
                        .flashAttr("rental", rentalDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rental"));

        verify(rentalService, times(1)).create(rentalDto);
    }

    @Test
    @Description(value = "Тест проверяет удаление аренды")
    @DisplayName("Тест удаление аренды")
    void deleteRentalByIdTest() throws Exception {
        mockMvc.perform(delete("/rental/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rental"));

        verify(rentalService, times(1)).delete(1);
    }

    @Test
    @Description(value = "Тест проверяет сохранение обновления аренды")
    @DisplayName("Тест обновления аренды")
    void updateRentalByIdTest() throws Exception {
        RentalDto rentalDto = new RentalDto();
        rentalDto.setId(1);

        mockMvc.perform(patch("/rental/1")
                        .flashAttr("rental", rentalDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rental"));

        verify(rentalService, times(1)).update(1, rentalDto);
    }

    @Test
    @Description(value = "Тест проверяет открытия страницы rental/update выбранной аренды")
    @DisplayName("Тест rental/update")
    void showUpdateFormTest() throws Exception {
        RentalDto rental = new RentalDto();
        rental.setId(1);
        when(rentalService.findById(1)).thenReturn(Optional.of(rental));

        mockMvc.perform(get("/rental/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("rental/update"))
                .andExpect(model().attributeExists("rental"))
                .andExpect(model().attribute("rental", rental));

        verify(rentalService, times(1)).findById(1);
    }
}
