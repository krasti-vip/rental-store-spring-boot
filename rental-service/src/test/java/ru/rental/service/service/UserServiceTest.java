package ru.rental.service.service;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rental.service.BaseBd;
import ru.rental.service.dto.UserDto;
import ru.rental.service.dto.create.UserDtoCreate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Тест UserService")
class UserServiceTest extends BaseBd {

    @Autowired
    private UserService userService;

    @Test
    @Description(value = "Тест проверяет существование пользователя, затем что возвращается правильный пользователь по айди")
    @DisplayName("Тест findById() для пользователя")
    void findBiId() {
        assertTrue(userService.findById(2).isPresent());
        assertEquals("tom", userService.findById(2).get().getUserName());
    }

    @Test
    @Description(value = "Тест проверяет сохранение и удаление пользователя")
    @DisplayName("Тест create() и delete() для пользователя")
    void createAndDeleteTest() {

        UserDtoCreate userDtoCreate = new UserDtoCreate(
                "user",
                "name",
                "test",
                658743459L,
                null,
                null,
                null,
                null,
                null
        );
        assertEquals(5, userService.getAll().size());
        userService.create(userDtoCreate);
        assertEquals(6, userService.getAll().size());
        assertEquals("test", userService.findById(6).get().getLastName());

        assertTrue(userService.delete(6));
        assertEquals(5, userService.getAll().size());
    }

    @Test
    @Description("Тест проверяет обновление пользователя")
    @DisplayName("Тест userUpdate() для пользователя")
    void userUpdateTest() {

        UserDto userDto = new UserDto(
                3,
                "user",
                "name",
                "test",
                123456L,
                null,
                null,
                null,
                null,
                null
        );

        assertEquals("jerry", userService.findById(3).get().getUserName());
        userService.update(userDto);
        assertEquals("user", userService.findById(3).get().getUserName());
    }

    @Test
    @Description(value = "Тест проверяет получение всех пользователей")
    @DisplayName("Тест getAll() для пользователей")
    void getAllTest() {
        List<UserDto> user = userService.getAll();
        assertFalse(user.isEmpty());
        assertEquals(5, user.size());
    }
}
