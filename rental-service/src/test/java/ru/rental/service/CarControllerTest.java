package ru.rental.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.rental.service.controller.CarController;
import ru.rental.service.dto.CarDto;
import ru.rental.service.service.CarService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CarControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CarService carService;

    @InjectMocks
    private CarController carController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
    }

    @Test
    void getAllCars_ShouldReturnCarsTest() throws Exception {
        when(carService.getAll()).thenReturn(Collections.singletonList(new CarDto()));

        mockMvc.perform(get("/car"))
                .andExpect(status().isOk())
                .andExpect(view().name("car/index"))
                .andExpect(model().attributeExists("cars"));

        verify(carService, times(1)).getAll();
    }

    @Test
    void getCarById_ShouldReturnCarTest() throws Exception {
        CarDto car = new CarDto();
        car.setId(1);
        when(carService.get(1)).thenReturn(Optional.of(car));

        mockMvc.perform(get("/car/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("car/car"))
                .andExpect(model().attributeExists("car"))
                .andExpect(model().attribute("car", car));

        verify(carService, times(1)).get(1);
    }

    @Test
    void getCarByIdTest() throws Exception {
        when(carService.get(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/car/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("car no found id: 1"));

        verify(carService, times(1)).get(1);
    }

    @Test
    void getCreateCarFormTest() throws Exception {
        mockMvc.perform(get("/car/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("car/form"))
                .andExpect(model().attributeExists("car"));
    }

    @Test
    void createCarTest() throws Exception {
        CarDto carDto = new CarDto();
        carDto.setId(1);

        mockMvc.perform(post("/car")
                        .flashAttr("car", carDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/car"));

        verify(carService, times(1)).save(carDto);
    }

    @Test
    void deleteCarByIdTest() throws Exception {
        mockMvc.perform(delete("/car/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/car"));

        verify(carService, times(1)).delete(1);
    }

    @Test
    void updateCarByIdTest() throws Exception {
        CarDto carDto = new CarDto();
        carDto.setId(1);

        mockMvc.perform(patch("/car/1")
                        .flashAttr("car", carDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/car"));

        verify(carService, times(1)).update(1, carDto);
    }

    @Test
    void showUpdateFormTest() throws Exception {
        CarDto car = new CarDto();
        car.setId(1);
        when(carService.get(1)).thenReturn(Optional.of(car));

        mockMvc.perform(get("/car/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("car/update"))
                .andExpect(model().attributeExists("car"))
                .andExpect(model().attribute("car", car));

        verify(carService, times(1)).get(1);
    }
}
