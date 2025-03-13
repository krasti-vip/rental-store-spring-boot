package ru.rental.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.rental.service.controller.BikeController;
import ru.rental.service.dto.BikeDto;
import ru.rental.service.service.BikeService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BikeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BikeService bikeService;

    @InjectMocks
    private BikeController bikeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bikeController).build();
    }

    @Test
    void getAllBikes_ShouldReturnBikesTest() throws Exception {
        when(bikeService.getAll()).thenReturn(Collections.singletonList(new BikeDto()));

        mockMvc.perform(get("/bike"))
                .andExpect(status().isOk())
                .andExpect(view().name("bike/index"))
                .andExpect(model().attributeExists("bikes"));

        verify(bikeService, times(1)).getAll();
    }

    @Test
    void getBikeById_ShouldReturnBikeTest() throws Exception {
        BikeDto bike = new BikeDto();
        bike.setId(1);
        when(bikeService.get(1)).thenReturn(Optional.of(bike));

        mockMvc.perform(get("/bike/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bike/bike"))
                .andExpect(model().attributeExists("bike"))
                .andExpect(model().attribute("bike", bike));

        verify(bikeService, times(1)).get(1);
    }

    @Test
    void getBikeByIdTest() throws Exception {
        when(bikeService.get(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/bike/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("bike no found id: 1"));

        verify(bikeService, times(1)).get(1);
    }

    @Test
    void getCreateBikeFormTest() throws Exception {
        mockMvc.perform(get("/bike/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("bike/form"))
                .andExpect(model().attributeExists("bike"));
    }

    @Test
    void createBikeTest() throws Exception {
        BikeDto bikeDto = new BikeDto();
        bikeDto.setId(1);

        mockMvc.perform(post("/bike")
                        .flashAttr("bike", bikeDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bike"));

        verify(bikeService, times(1)).save(bikeDto);
    }

    @Test
    void deleteBikeByIdTest() throws Exception {
        mockMvc.perform(delete("/bike/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bike"));

        verify(bikeService, times(1)).delete(1);
    }

    @Test
    void updateBikeByIdTest() throws Exception {
        BikeDto bikeDto = new BikeDto();
        bikeDto.setId(1);

        mockMvc.perform(patch("/bike/1")
                        .flashAttr("bike", bikeDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bike"));

        verify(bikeService, times(1)).update(1, bikeDto);
    }

    @Test
    void showUpdateFormTest() throws Exception {
        BikeDto bike = new BikeDto();
        bike.setId(1);
        when(bikeService.get(1)).thenReturn(Optional.of(bike));

        mockMvc.perform(get("/bike/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("bike/update"))
                .andExpect(model().attributeExists("bike"))
                .andExpect(model().attribute("bike", bike));

        verify(bikeService, times(1)).get(1);
    }
}
