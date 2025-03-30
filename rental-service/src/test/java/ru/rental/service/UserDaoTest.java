package ru.rental.service;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rental.service.dao.UserDao;
import ru.rental.service.model.User;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Тест UserDao")
class UserDaoTest extends BaseBd {

    @Autowired
    private UserDao userDao;

    private static Stream<Arguments> sourceUser() {
        return Stream.of(
                Arguments.of(
                        User.builder()
                                .id(1)
                                .userName("bill")
                                .firstName("Ivanov")
                                .lastName("Dima")
                                .passport(456987123)
                                .email(null)
                                .build()
                ),
                Arguments.of(
                        User.builder()
                                .id(2)
                                .userName("tom")
                                .firstName("Sidorov")
                                .lastName("Pasha")
                                .passport(98741236)
                                .email(null)
                                .build()
                ),
                Arguments.of(
                        User.builder()
                                .id(3)
                                .userName("jerry")
                                .firstName("Petrov")
                                .lastName("Sasha")
                                .passport(12365478)
                                .email(null)
                                .build()
                ),
                Arguments.of(
                        User.builder()
                                .id(4)
                                .userName("ozi")
                                .firstName("Galcin")
                                .lastName("Gena")
                                .passport(56987415)
                                .email("gav@mail.ru")
                                .build()
                ),
                Arguments.of(
                        User.builder()
                                .id(5)
                                .userName("eminem")
                                .firstName("Pugachev")
                                .lastName("Genya")
                                .passport(85297418)
                                .email(null)
                                .build()
                )
        );
    }

    private static Stream<Arguments> sourceUserForFilterTest() {
        return Stream.of(
                Arguments.of(new User(1, "bill", "Ivanov", "Dima", 456987123,
                        null, null, null, null), "bill", "bill"),
                Arguments.of(new User(2, "tom", "Sidorov", "Pasha", 98741236,
                        null, null, null, null), "tom", "tom"),
                Arguments.of(new User(3, "jerry", "Petrov", "Sasha", 12365478,
                        null, null, null, null), "jerry", "jerry"),
                Arguments.of(new User(4, "ozi", "Galcin", "Gena", 56987415,
                        "gav@mail.ru", null, null, null), "ozi", "ozi"),
                Arguments.of(new User(5, "eminem", "Pugachev", "Genya", 85297418,
                        null, null, null, null), "eminem", "eminem")
        );
    }

    @Test
    @Description(value = "Тест проверяет возвращения всех пользователей")
    @DisplayName("Тест getAllUser")
    void userDaoGetAll() {
        final var allUsers = userDao.getAll();
        assertEquals(5, allUsers.size());
    }

    @Test
    @Description(value = "Тест проверяет была ли создана таблица users")
    @DisplayName("Тест создания таблицы users")
    void createTableTest() {
        userDao.createTable();
        boolean creatTrue = userDao.checkIfTableExists("users");
        assertTrue(creatTrue);
        boolean noCreat = userDao.checkIfTableExists("test");
        assertFalse(noCreat);
    }

    @ParameterizedTest
    @MethodSource("sourceUser")
    @Description(value = "Тест проверяет возвращения пользователей и что у них правильные поля")
    @DisplayName("Тест getUser")
    void getUserTest(User sourceUser) {
        final var user = userDao.get(sourceUser.getId());

        assertAll(
                () -> assertEquals(user.getId(), sourceUser.getId()),
                () -> assertEquals(user.getUserName(), sourceUser.getUserName()),
                () -> assertEquals(user.getFirstName(), sourceUser.getFirstName()),
                () -> assertEquals(user.getLastName(), sourceUser.getLastName()),
                () -> assertEquals(user.getPassport(), sourceUser.getPassport()),
                () -> assertEquals(user.getEmail(), sourceUser.getEmail())
        );
    }

    @ParameterizedTest
    @Description(value = "Тест проверяет обновление пользователей, а так же все их поля на соответствие изменений")
    @MethodSource("sourceUser")
    @DisplayName("Тест обновления пользователя")
    void updateUserTest(User sourceUser) {

        int uniquePassport = generateUniquePassport();

        User updatedUser = new User(
                sourceUser.getId(),
                sourceUser.getUserName(),
                sourceUser.getFirstName(),
                sourceUser.getLastName(),
                uniquePassport,
                sourceUser.getEmail(),
                sourceUser.getListBike(),
                sourceUser.getListCar(),
                sourceUser.getListBicycle()
        );

        User nonUser = new User(
                -1,
                sourceUser.getUserName(),
                sourceUser.getFirstName(),
                sourceUser.getLastName(),
                sourceUser.getPassport(),
                sourceUser.getEmail(),
                sourceUser.getListBike(),
                sourceUser.getListCar(),
                sourceUser.getListBicycle()
        );

        int nonUserId = nonUser.getId();

        final var updatedUserBd = userDao.update(sourceUser.getId(), updatedUser);
        final var userUpdate = userDao.get(updatedUserBd.getId());

        assertAll(
                () -> assertEquals(userUpdate.getId(), updatedUser.getId()),
                () -> assertEquals(userUpdate.getUserName(), updatedUser.getUserName()),
                () -> assertEquals(userUpdate.getFirstName(), updatedUser.getFirstName()),
                () -> assertEquals(userUpdate.getLastName(), updatedUser.getLastName()),
                () -> assertEquals(userUpdate.getPassport(), updatedUser.getPassport()),
                () -> assertEquals(userUpdate.getEmail(), updatedUser.getEmail()),
                () -> assertThrows(IllegalStateException.class, () -> {
                    userDao.update(nonUserId, updatedUser);
                }, "Expected update to throw an exception as the user does not exist")
        );
    }

    @ParameterizedTest
    @MethodSource("sourceUser")
    @Description(value = "Тест проверяет сохранение пользователя, а затем его удаление и что количество пользователей сначала " +
                         "изменилось, а потом вернулась")
    @DisplayName("Тест добавления и удаления пользователя")
    void saveAndDeleteUserTest(User sourceUser) {
        int uniquePassport = generateUniquePassport();

        User userToSave = new User(
                -896,
                sourceUser.getUserName(),
                sourceUser.getFirstName(),
                sourceUser.getLastName(),
                uniquePassport,
                sourceUser.getEmail(),
                sourceUser.getListBike(),
                sourceUser.getListCar(),
                sourceUser.getListBicycle()
        );

        User savedUser = userDao.save(userToSave);
        assertNotNull(savedUser, "User должен быть успешно сохранен");

        assertEquals(6, userDao.getAll().size(), "После сохранения в базе должно быть 6 users");
        assertTrue(userDao.getAll().stream().anyMatch(b -> b.getId() == savedUser.getId()), "Сохраненный user должен быть в списке");

        User savedUserFromDb = userDao.get(savedUser.getId());
        assertAll(
                () -> assertEquals(userToSave.getUserName(), savedUserFromDb.getUserName(), "Имя user должно совпадать"),
                () -> assertEquals(userToSave.getFirstName(), savedUserFromDb.getFirstName(), "firstName должен совпадать"),
                () -> assertEquals(userToSave.getLastName(), savedUserFromDb.getLastName(), "LastName должен совпадать"),
                () -> assertEquals(userToSave.getPassport(), savedUserFromDb.getPassport(), "Номер Passport должен совпадать"),
                () -> assertEquals(userToSave.getEmail(), savedUserFromDb.getEmail(), "Email должен совпадать")
        );

        assertTrue(userDao.delete(savedUser.getId()), "User должен быть успешно удален");
        assertFalse(userDao.delete(999), "Попытка удаления несуществующего user должна вернуть false");
        assertEquals(5, userDao.getAll().size(), "После удаления user в базе должно быть 5 users");
    }

    @ParameterizedTest
    @MethodSource("sourceUserForFilterTest")
    @Description(value = "Тест проверяет фильтрацию базы пользователей по предикату")
    @DisplayName("Test filterUser")
    void filtrUserTest(User sourceUserForFilterTest, String filterKeyword, String expectedName) {
        Predicate<User> predicate = user -> user.getUserName().contains(filterKeyword);

        List<User> filteredUsers = userDao.filterBy(predicate);

        assertTrue(filteredUsers.stream().anyMatch(b -> b.getUserName().equals(expectedName)),
                "Отфильтрованный user должен быть " + expectedName);
    }

    private int generateUniquePassport() {
        Random random = new Random();
        return random.nextInt(1000000);
    }
}
