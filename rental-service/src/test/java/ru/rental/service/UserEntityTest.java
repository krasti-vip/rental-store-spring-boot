package ru.rental.service;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.rental.service.entity.User;
import ru.rental.service.repository.UserRepository;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Тест UserDao")
class UserEntityTest extends BaseBd {

    @Autowired
    private UserRepository userRepository;

    private static Stream<Arguments> sourceUser() {
        return Stream.of(
                Arguments.of(
                        User.builder()
                                .id(1)
                                .userName("bill")
                                .firstName("Ivanov")
                                .lastName("Dima")
                                .passport(456987123L)
                                .email(null)
                                .build()
                ),
                Arguments.of(
                        User.builder()
                                .id(2)
                                .userName("tom")
                                .firstName("Sidorov")
                                .lastName("Pasha")
                                .passport(98741236L)
                                .email(null)
                                .build()
                ),
                Arguments.of(
                        User.builder()
                                .id(3)
                                .userName("jerry")
                                .firstName("Petrov")
                                .lastName("Sasha")
                                .passport(12365478L)
                                .email(null)
                                .build()
                ),
                Arguments.of(
                        User.builder()
                                .id(4)
                                .userName("ozi")
                                .firstName("Galcin")
                                .lastName("Gena")
                                .passport(56987415L)
                                .email("gav@mail.ru")
                                .build()
                ),
                Arguments.of(
                        User.builder()
                                .id(5)
                                .userName("eminem")
                                .firstName("Pugachev")
                                .lastName("Genya")
                                .passport(85297418L)
                                .email(null)
                                .build()
                )
        );
    }

    @Test
    @Description(value = "Тест проверяет возвращения всех пользователей")
    @DisplayName("Тест getAllUser")
    void userDaoGetAll() {
        final List<User> allUsers = (List<User>) userRepository.findAll();
        assertEquals(5, allUsers.size());
    }

    @ParameterizedTest
    @MethodSource("sourceUser")
    @Description(value = "Тест проверяет возвращения пользователей и что у них правильные поля")
    @DisplayName("Тест getUser")
    void getUserTest(User sourceUser) {
        final var user = userRepository.findById(sourceUser.getId());

        assertAll(
                () -> assertEquals(user.get().getId(), sourceUser.getId()),
                () -> assertEquals(user.get().getUserName(), sourceUser.getUserName()),
                () -> assertEquals(user.get().getFirstName(), sourceUser.getFirstName()),
                () -> assertEquals(user.get().getLastName(), sourceUser.getLastName()),
                () -> assertEquals(user.get().getPassport(), sourceUser.getPassport()),
                () -> assertEquals(user.get().getEmail(), sourceUser.getEmail())
        );
    }

    @ParameterizedTest
    @Description(value = "Тест проверяет обновление пользователей, а так же все их поля на соответствие изменений")
    @MethodSource("sourceUser")
    @DisplayName("Тест обновления пользователя")
    void updateUserTest(User sourceUser) {

        Long uniquePassport = 123456L;

        User updatedUser = new User(
                sourceUser.getId(),
                sourceUser.getUserName(),
                sourceUser.getFirstName(),
                sourceUser.getLastName(),
                uniquePassport,
                sourceUser.getEmail(),
                sourceUser.getBankCards(),
                sourceUser.getBikes(),
                sourceUser.getCars(),
                sourceUser.getBicycles()
        );

        userRepository.save(updatedUser);
        final var userUpdate = userRepository.findById(sourceUser.getId());

        assertAll(
                () -> assertEquals(userUpdate.get().getId(), updatedUser.getId()),
                () -> assertEquals(userUpdate.get().getUserName(), updatedUser.getUserName()),
                () -> assertEquals(userUpdate.get().getUserName(), updatedUser.getFirstName()),
                () -> assertEquals(userUpdate.get().getFirstName(), updatedUser.getLastName()),
                () -> assertEquals(userUpdate.get().getPassport(), updatedUser.getPassport()),
                () -> assertEquals(userUpdate.get().getEmail(), updatedUser.getEmail())
        );

        int nonExistentUserId = -1;

        assertFalse(
                userRepository.existsById(nonExistentUserId),
                "Пользователь с несуществующим ID не должен быть в БД"
        );
    }

    @ParameterizedTest
    @MethodSource("sourceUser")
    @Description(value = "Тест проверяет сохранение пользователя, а затем его удаление и что количество пользователей сначала " +
                         "изменилось, а потом вернулась")
    @DisplayName("Тест добавления и удаления пользователя")
    void saveAndDeleteUserTest(User sourceUser) {
        Long uniquePassport = 123456L;

        User userToSave = new User(
                9,
                sourceUser.getUserName(),
                sourceUser.getFirstName(),
                sourceUser.getLastName(),
                uniquePassport,
                sourceUser.getEmail(),
                sourceUser.getBankCards(),
                sourceUser.getBikes(),
                sourceUser.getCars(),
                sourceUser.getBicycles()
        );

        User savedUser = userRepository.save(userToSave);
        assertNotNull(savedUser, "User должен быть успешно сохранен");

        List<User> allUsers = StreamSupport.stream(
                userRepository.findAll().spliterator(),
                false
        ).toList();

        assertEquals(6, allUsers.size(), "После сохранения в базе должно быть 6 users");
        assertTrue(userRepository.existsById(savedUser.getId()), "Сохраненный user должен быть в списке");

        User savedUserFromDb = userRepository.findById(savedUser.getId())
                        .orElseThrow(() -> new AssertionError("<UNK> <UNK> <UNK> <UNK> <UNK>"));
        assertAll(
                () -> assertEquals(userToSave.getUserName(), savedUserFromDb.getUserName(), "Имя user должно совпадать"),
                () -> assertEquals(userToSave.getFirstName(), savedUserFromDb.getFirstName(), "firstName должен совпадать"),
                () -> assertEquals(userToSave.getLastName(), savedUserFromDb.getLastName(), "LastName должен совпадать"),
                () -> assertEquals(userToSave.getPassport(), savedUserFromDb.getPassport(), "Номер Passport должен совпадать"),
                () -> assertEquals(userToSave.getEmail(), savedUserFromDb.getEmail(), "Email должен совпадать")
        );

        userRepository.deleteById(savedUser.getId());
        assertFalse(userRepository.existsById(savedUser.getId()), "User должен быть удалён из базы данных");

        List<User> allUser = StreamSupport.stream(
                userRepository.findAll().spliterator(),
                false
        ).toList();

        assertEquals(5, allUser.size(), "После удаления user в базе должно быть 5 users");

        try {
            userRepository.deleteById(999);
        } catch (EmptyResultDataAccessException e) {
        }
    }
}
