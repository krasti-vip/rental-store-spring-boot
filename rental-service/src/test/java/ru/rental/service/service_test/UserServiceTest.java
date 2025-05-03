package ru.rental.service.service_test;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rental.service.BaseBd;
import ru.rental.service.dto.UserDto;
import ru.rental.service.service.UserService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Тест UserService")
class UserServiceTest extends BaseBd {

    @Autowired
    private UserService userService;

    @Test
    @Description(value = "Тест проверяет существование пользователя, затем что возвращается правильный пользователь по айди")
    @DisplayName("Тест get")
    void getTest() {
        Integer userId = userService.getAll().get(3).getId();
        Optional<UserDto> user = userService.findById(userId);
        assertTrue(user.isPresent(), "Пользователь с id должен существовать");
        assertEquals("ozi", userService.findById(userId).get().getUserName());
    }

    @Test
    @Description("Тест проверяет частичное обновление пользователя через Map")
    @DisplayName("Тест update() для частичного обновления пользователя")
    void userUpdateTest() {

    }

    @Test
    @Description(value = "Тест проверяет сохранение пользователя, а затем его удаление и что количество пользователей сначала " +
                         "изменилось, а потом вернулась")
    @DisplayName("Тест сохранения и удаления пользователя")
    void saveAndDeleteTest() {

    }

    @Test
    @Description(value = "Тест проверяет возвращение всех пользователей и что список больше - 0")
    @DisplayName("Тест возвращения всех пользователей")
    void getAllTest() {
        List<UserDto> users = userService.getAll();
        assertTrue(users.size() > 0);
    }
}
