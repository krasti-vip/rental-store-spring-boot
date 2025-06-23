package ru.rental.service.user.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.rental.service.common.dto.UserDto;
import ru.rental.service.common.dto.UserDtoCreate;
import ru.rental.service.user.BaseBd;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserRestTest extends BaseBd {

    @Autowired
    private UserControllerRest userController;

    @Test
    @Description(value = "Тест findById() для пользователя через RestController")
    @DisplayName("Тест RestController для пользователя")
    void findByIdTest() {
//        ResponseEntity<UserDto> response = userController.findById(1);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        UserDto body = response.getBody();
//        assertNotNull(body);
//        assertEquals(1, body.getId());
//        assertEquals("bill", body.getUserName());
//        assertEquals("Ivanov", body.getFirstName());
//        assertEquals("Dima", body.getLastName());
//        assertEquals(456987123L, body.getPassport());
    }

    @Test
    @Description(value = "Тест findAll() для пользователя через RestController")
    @DisplayName("Тест findAll() для пользователей")
    void findAllTest() {
        ResponseEntity<List<UserDto>> response = userController.findAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<UserDto> users = response.getBody();
        assertNotNull(users);
        assertEquals(5, users.size());
        boolean hasBill = users.stream().anyMatch(u -> "bill".equals(u.getUserName()));
        assertTrue(hasBill, "В списке должен быть пользователь 'bill'");
    }

    @Test
    @Description(value = "Тест create() для пользователя через RestController")
    @DisplayName("Тест create() для пользователя")
    void createTest() {
        UserDtoCreate newUser = new UserDtoCreate();
        newUser.setUserName("newuser");
        newUser.setFirstName("First");
        newUser.setLastName("Last");
        newUser.setPassport(999999999L);
        newUser.setEmail("newuser@mail.com");
        ResponseEntity<UserDto> response = userController.create(newUser);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        UserDto created = response.getBody();
        assertNotNull(created);
        assertEquals("newuser", created.getUserName());
        assertEquals("First", created.getFirstName());
        assertEquals("Last", created.getLastName());
        assertEquals(999999999L, created.getPassport());
        assertEquals("newuser@mail.com", created.getEmail());
        assertTrue(created.getId() > 0); // id должен быть сгенерирован
    }

    @Test
    @Description(value = "Тест delete() для пользователя через RestController")
    @DisplayName("Тест delete() для пользователя")
    void deleteTest() {
        ResponseEntity<Void> response = userController.delete(5);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        ResponseEntity<UserDto> afterDelete = userController.findById(5);
        assertEquals(HttpStatus.NOT_FOUND, afterDelete.getStatusCode());
    }

    @Test
    @Description(value = "Тест update() для пользователя через RestController")
    @DisplayName("Тест updateUser() для пользователя")
    void updateTest() {
        UserDto update = new UserDto();
        update.setId(1);
        update.setUserName("billUpdated");
        update.setFirstName("IvanovUpdated");
        update.setLastName("DimaUpdated");
        update.setPassport(456987123L);
        update.setEmail("billupdated@mail.com");
        ResponseEntity<UserDto> response = userController.updateUser(1, update);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserDto updatedUser = response.getBody();
        assertNotNull(updatedUser);
        assertEquals("billUpdated", updatedUser.getUserName());
        assertEquals("IvanovUpdated", updatedUser.getFirstName());
        assertEquals("DimaUpdated", updatedUser.getLastName());
        assertEquals("billupdated@mail.com", updatedUser.getEmail());
    }

    @Test
    @Description(value = "Тест проверяет валидацию при неверно переданных параметрах для пользователя через RestController")
    @DisplayName("Тест updateUser() с неверными данными - ожидание ConstraintViolationException")
    void updateUserValidationErrorTest() {
        UserDto update = new UserDto();
        update.setUserName("");
        assertThrows(IllegalArgumentException.class, () -> {
            userController.updateUser(1, update);
        });
    }
}
