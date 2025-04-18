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
import ru.rental.service.controller.BicycleController;
import ru.rental.service.dto.BicycleDto;
import ru.rental.service.service.BicycleService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@DisplayName("Тест BicycleController")
class BicycleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BicycleService bicycleService;

    @InjectMocks
    private BicycleController bicycleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bicycleController).build();
    }

    @Test
    @Description(value = "Тест проверяет возвращение всех велосипедов на главную страницу")
    @DisplayName("Тест getAll для bicycle/index")
    void getAllBicycleTest() throws Exception {
        when(bicycleService.getAll()).thenReturn(Collections.singletonList(new BicycleDto()));

        mockMvc.perform(get("/bicycle"))
                .andExpect(status().isOk())
                .andExpect(view().name("bicycle/index"))
                .andExpect(model().attributeExists("bicycles"));

        verify(bicycleService, times(1)).getAll();
    }

    @Test
    @Description(value = "Тест проверяет возвращение велосипеда по его айди на новую страницу")
    @DisplayName("Тест get для bicycle/1")
    void getBicycleById_ShouldReturnTest() throws Exception {
        BicycleDto bicycleDto = new BicycleDto();
        bicycleDto.setId(1);
        when(bicycleService.findById(1)).thenReturn(Optional.of(bicycleDto));

        mockMvc.perform(get("/bicycle/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bicycle/bicycle"))
                .andExpect(model().attributeExists("bicycle"))
                .andExpect(model().attribute("bicycle", bicycleDto));

        verify(bicycleService, times(1)).findById(1);
    }

    @Test
    @Description(value = "Тест проверяет возвращение велосипеда по айди и что он не пустой")
    @DisplayName("Тест get для bicycle/1 no found")
    void getBicycleByIdTest() throws Exception {
        when(bicycleService.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/bicycle/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("bicycle no found id: 1"));

        verify(bicycleService, times(1)).findById(1);
    }

    @Test
    @Description(value = "Тест проверяет открытия страницы добавления велосипеда bicycle/form")
    @DisplayName("Тест bicycle/create")
    void getCreateBicycleFormTest() throws Exception {
        mockMvc.perform(get("/bicycle/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("bicycle/form"))
                .andExpect(model().attributeExists("bicycle"));
    }

    @Test
    @Description(value = "Тест проверяет сохранение нового велосипеда")
    @DisplayName("Тест сохранения велосипеда")
    void createBicycleTest() throws Exception {
        BicycleDto bicycleDto = new BicycleDto();
        bicycleDto.setId(1);

        mockMvc.perform(post("/bicycle")
                        .flashAttr("bicycle", bicycleDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bicycle"));

        verify(bicycleService, times(1)).create(bicycleDto);
    }

    @Test
    @Description(value = "Тест проверяет удаление велосипеда")
    @DisplayName("Тест удаление велосипеда")
    void deleteBicycleByIdTest() throws Exception {
        mockMvc.perform(delete("/bicycle/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bicycle"));

        verify(bicycleService, times(1)).delete(1);
    }

    @Test
    @Description(value = "Тест проверяет сохранение обновления велосипеда")
    @DisplayName("Тест обновления велосипеда")
    void updateBicycleByIdTest() throws Exception {
        BicycleDto bicycleDto = new BicycleDto();
        bicycleDto.setId(1);

        mockMvc.perform(patch("/bicycle/1")
                        .flashAttr("bicycle", bicycleDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bicycle"));

        verify(bicycleService, times(1)).update(1, bicycleDto);
    }

    @Test
    @Description(value = "Тест проверяет открытия страницы bicycle/update выбранного велосипеда")
    @DisplayName("Тест bicycle/update")
    void showUpdateFormTest() throws Exception {
        BicycleDto bicycleDto = new BicycleDto();
        bicycleDto.setId(1);
        when(bicycleService.findById(1)).thenReturn(Optional.of(bicycleDto));

        mockMvc.perform(get("/bicycle/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("bicycle/update"))
                .andExpect(model().attributeExists("bicycle"))
                .andExpect(model().attribute("bicycle", bicycleDto));

        verify(bicycleService, times(1)).findById(1);
    }
}
