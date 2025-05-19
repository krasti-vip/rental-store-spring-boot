package ru.rental.service.service;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rental.service.BaseBd;
import ru.rental.service.dto.BikeDto;
import ru.rental.service.dto.create.BikeDtoCreeate;
import ru.rental.service.dto.create.UserDtoCreate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Тест BikeService")
class BikeServiceTest extends BaseBd {

    @Autowired
    BikeService bikeService;

    @Autowired
    UserService userService;

    @Test
    @Description(value = "Тест проверяет существование мотоцикла, затем что возвращается правильный мотоцикл по айди")
    @DisplayName("Тест получения мотоцикла по ID")
    void findBiId() {
        assertTrue(bikeService.findById(1).isPresent());
        assertEquals("BMW", bikeService.findById(1).get().getName());
    }

    @Test
    @Description(value = "Тест проверяет сохранение и удаление мотоцикла")
    @DisplayName("Тест create() и delete() для мотоцикла")
    void createAndDeleteTest() {
        UserDtoCreate userDtoCreate = new UserDtoCreate(
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
        userService.create(userDtoCreate);

        BikeDtoCreeate bikeDtoCreeate = new BikeDtoCreeate(
                "Harley",
                54.5,
                75,
                0.75,
                6
        );
        assertEquals(5, bikeService.getAll().size());
        bikeService.create(bikeDtoCreeate);
        assertEquals(6, bikeService.getAll().size());
        assertEquals("Harley", bikeService.findById(6).get().getName());

        assertTrue(bikeService.delete(6));
        assertEquals(5, bikeService.getAll().size());
    }

    @Test
    @Description("Тест проверяет обновление мотоцикла")
    @DisplayName("Тест bikeUpdate() для мотоцикла")
    void bikeUpdateTest() {

        BikeDto bikeDto = new BikeDto(
                2,
                "YAMAHA",
                40000,
                400,
                1.0,
                1
        );

        assertEquals(2, bikeService.findByUserId(1).size());
        bikeService.update(bikeDto);
        assertEquals(3, bikeService.findByUserId(1).size());
    }

    @Test
    @Description(value = "Тест проверяет получение всех мотоциклов")
    @DisplayName("Тест getAll() для мотоциклов")
    void getAllTest() {
        List<BikeDto> bike = bikeService.getAll();
        assertFalse(bike.isEmpty());
        assertEquals(5, bike.size());
    }
}
