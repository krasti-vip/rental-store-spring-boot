package ru.rental.service;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rental.service.dto.UserDto;
import ru.rental.service.service.UserService;

import java.util.List;
import java.util.Optional;

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
        Integer userId = userService.findAll().get(3).getId();
        Optional<UserDto> user = userService.findById(userId);
        assertTrue(user.isPresent(), "Пользователь с id должен существовать");
        assertEquals("ozi", userService.findById(userId).get().getUserName());
        assertThrows(IllegalArgumentException.class, () -> {
            userService.findById(null);
        });
    }

    @Test
    @Description(value = "Тест проверяет обновление пользователя, а также соответствие полей после обновления")
    @DisplayName("Тест обновления пользователя")
    void updateTest() {
        Integer userId = userService.findAll().get(3).getId();
        UserDto userTest2 = UserDto.builder()
                .userName("Jac")
                .firstName("Boy")
                .lastName("Vas")
                .passport(72621)
                .email("krasti@yandex.ru")
                .bikes(List.of())
                .cars(List.of())
                .bicycles(List.of())
                .build();
        userService.update(userId, userTest2);
        assertEquals("Jac", userService.findById(userId).get().getUserName());
    }

    @Test
    @Description(value = "Тест проверяет сохранение пользователя, а затем его удаление и что количество пользователей сначала " +
                         "изменилось, а потом вернулась")
    @DisplayName("Тест сохранения и удаления пользователя")
    void saveAndDeleteTest() {
        UserDto userDto = UserDto.builder()
                .userName("vandervud")
                .lastName("Tom")
                .firstName("Hardi")
                .passport(85234789)
                .email("hardi@mail.ru")
                .build();

        int userDtoId = userService.create(userDto).getId();
        assertEquals("vandervud", userService.findById(userDtoId).get().getUserName());
        assertEquals(6, userService.findAll().size());

        Integer userId = userService.findAll().get(5).getId();
        assertTrue(userService.findById(userId).isPresent());
        assertEquals(6, userService.findAll().size());
        userService.delete(userId);
        Optional<UserDto> user = userService.findById(userId);
        assertTrue(user.isEmpty());
        assertEquals(5, userService.findAll().size());
    }

    @Test
    @Description(value = "Тест проверяет возвращение всех пользователей и что список больше - 0")
    @DisplayName("Тест возвращения всех пользователей")
    void getAllTest() {
        List<UserDto> users = userService.findAll();
        assertTrue(users.size() > 0);
    }
}
