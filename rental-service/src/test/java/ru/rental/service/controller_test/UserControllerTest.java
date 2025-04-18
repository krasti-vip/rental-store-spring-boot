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
import ru.rental.service.controller.UserController;
import ru.rental.service.dto.UserDto;
import ru.rental.service.service.UserService;

import java.util.Collections;
import java.util.Optional;

import static java.lang.Thread.sleep;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@DisplayName("Тест UserController")
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @Description(value = "Тест проверяет возвращение всех пользователей на главную страницу")
    @DisplayName("Тест getAll для user/index")
    void getAllUserTest() throws Exception {
        when(userService.findAll()).thenReturn(Collections.singletonList(new UserDto()));

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/index"))
                .andExpect(model().attributeExists("users"));

        verify(userService, times(1)).findAll();
    }

    @Test
    @Description(value = "Тест проверяет возвращение пользователя по его айди на новую страницу")
    @DisplayName("Тест get для user/1")
    void getUserById_ShouldReturnTest() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1);
        when(userService.findById(1)).thenReturn(Optional.of(userDto));

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/user"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", userDto));

        verify(userService, times(1)).findById(1);
    }

    @Test
    @Description(value = "Тест проверяет возвращение пользователя по айди и что он не пустой")
    @DisplayName("Тест get для user/1 no found")
    void getUserByIdTest() throws Exception {
        when(userService.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("user no found id: 1"));

        verify(userService, times(1)).findById(1);
    }

    @Test
    @Description(value = "Тест проверяет открытия страницы добавления пользователя user/form")
    @DisplayName("Тест user/create")
    void getCreateUserFormTest() throws Exception {
        mockMvc.perform(get("/user/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/form"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @Description(value = "Тест проверяет сохранение нового пользователя")
    @DisplayName("Тест сохранения пользователя")
    void createUserTest() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1);

        mockMvc.perform(post("/user")
                        .flashAttr("user", userDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user"));

        verify(userService, times(1)).create(userDto);
    }

    @Test
    @Description(value = "Тест проверяет удаление пользователя")
    @DisplayName("Тест удаление пользователя")
    void deleteUserByIdTest() throws Exception {
        mockMvc.perform(delete("/user/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user"));

        verify(userService, times(1)).delete(1);
    }

    @Test
    @Description(value = "Тест проверяет сохранение обновления пользователя")
    @DisplayName("Тест обновления пользователя")
    void updateUserByIdTest() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1);

        mockMvc.perform(patch("/user/1")
                        .flashAttr("user", userDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user"));

        verify(userService, times(1)).update(1, userDto);
    }

    @Test
    @Description(value = "Тест проверяет открытия страницы user/update выбранного пользователя")
    @DisplayName("Тест user/update")
    void showUpdateFormTest() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1);
        when(userService.findById(1)).thenReturn(Optional.of(userDto));

        mockMvc.perform(get("/user/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", userDto));

        verify(userService, times(1)).findById(1);
    }
}
