package ru.rental.service;

import org.junit.jupiter.api.BeforeEach;
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
    void getAllRentalTest() throws Exception {
        when(rentalService.getAll()).thenReturn(Collections.singletonList(new RentalDto()));

        mockMvc.perform(get("/rental"))
                .andExpect(status().isOk())
                .andExpect(view().name("rental/index"))
                .andExpect(model().attributeExists("rentals"));

        verify(rentalService, times(1)).getAll();
    }

    @Test
    void getRentalByIdTest() throws Exception {
        RentalDto rental = new RentalDto();
        rental.setId(1);
        when(rentalService.get(1)).thenReturn(Optional.of(rental));

        mockMvc.perform(get("/rental/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("rental/rental"))
                .andExpect(model().attributeExists("rental"))
                .andExpect(model().attribute("rental", rental));

        verify(rentalService, times(1)).get(1);
    }

    @Test
    void getRentalById_NotFoundTest() throws Exception {
        when(rentalService.get(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/rental/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("rental no found id: 1"));

        verify(rentalService, times(1)).get(1);
    }

    @Test
    void createRentalTest() throws Exception {
        RentalDto rentalDto = new RentalDto();
        rentalDto.setId(1);

        mockMvc.perform(post("/rental")
                        .flashAttr("rental", rentalDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rental"));

        verify(rentalService, times(1)).save(rentalDto);
    }

    @Test
    void deleteRentalByIdTest() throws Exception {
        mockMvc.perform(delete("/rental/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rental"));

        verify(rentalService, times(1)).delete(1);
    }

    @Test
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
    void getCreateRentalFormTest() throws Exception {
        mockMvc.perform(get("/rental/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("rental/form"))
                .andExpect(model().attributeExists("rental"));
    }

    @Test
    void showUpdateFormTest() throws Exception {
        RentalDto rental = new RentalDto();
        rental.setId(1);
        when(rentalService.get(1)).thenReturn(Optional.of(rental));

        mockMvc.perform(get("/rental/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("rental/update"))
                .andExpect(model().attributeExists("rental"))
                .andExpect(model().attribute("rental", rental));

        verify(rentalService, times(1)).get(1);
    }

    @Test
    void showUpdateForm_NotFoundTest() throws Exception {
        when(rentalService.get(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/rental/1/edit"))
                .andExpect(status().isNotFound());

        verify(rentalService, times(1)).get(1);
    }
}
