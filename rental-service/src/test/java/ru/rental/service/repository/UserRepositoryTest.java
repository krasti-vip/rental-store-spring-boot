package ru.rental.service.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rental.service.BaseBd;
import ru.rental.service.entity.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserRepositoryTest extends BaseBd {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Тест получения всех пользователей")
    void findAllTest() {
        List<User> users = (List<User>) userRepository.findAll();
        assertEquals(5, users.size());
    }

    @Test
    @Transactional
    @DisplayName("Тест сохранения и удаления нового пользователя")
    void saveAndDeleteTest() {
        User user = new User(
                6,
                "Lina",
                "Nurala",
                "Genya",
                876509876567L,
                "ktr@uy.ru",
                null,
                null,
                null,
                null
        );
        userRepository.save(user);
        List<User> users = (List<User>) userRepository.findAll();
        assertEquals(6, users.size());

        userRepository.deleteById(6);
        users = (List<User>) userRepository.findAll();
        assertEquals(5, users.size());
    }

    @Test
    @DisplayName("Тест возвращения пользователя по id")
    void findByIdTest() {
        User user = userRepository.findById(1).orElseThrow();
        assertEquals("bill", user.getUserName());
    }

    @Test
    @DisplayName("Тест обновление пользователя")
    void updateUserTest() {
        User user = userRepository.findById(1).orElseThrow();
        user.setUserName("trup");
        userRepository.save(user);
        User updatedUser = userRepository.findById(1).orElseThrow();
        assertEquals("trup", updatedUser.getUserName());
    }
}

