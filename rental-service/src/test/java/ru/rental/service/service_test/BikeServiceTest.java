package ru.rental.service.service_test;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rental.service.BaseBd;
import ru.rental.service.dto.BikeDto;
import ru.rental.service.service.BikeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тест BikeService")
@SpringBootTest
class BikeServiceTest extends BaseBd {

    @Autowired
    BikeService bikeService;

    @Test
    @Description(value = "Тест проверяет существование мотоцикла, затем что возвращается правильный мотоцикл по айди")
    @DisplayName("Тест получения мотоцикла по ID")
    void getTest() {
        Integer bikeId = bikeService.getAll().get(3).getId();
        Optional<BikeDto> bikeDto = bikeService.findById(bikeId);
        assertTrue(bikeDto.isPresent(), "bike с bikeId должен существовать");
        assertEquals("URAL", bikeService.findById(bikeId).get().getName());
    }

    @Test
    @Description("Тест проверяет частичное обновление мотоцикла через Map")
    @DisplayName("Тест update() для частичного обновления мотоцикла")
    void bikeUpdateTest() {

    }

    @Test
    @Description(value = "Тест проверяет сохранение мотоцикла, а затем его удаление и что количество мотоциклов сначала " +
                         "изменилось, а потом вернулась")
    @DisplayName("Тест сохранения, а затем удаление мотоцикла")
    void saveAndDeleteTest() {

    }

    @Test
    @Description(value = "Тест проверяет возвращение всех мотоциклов")
    @DisplayName("Тест возвращения всех мотоциклов")
    void getAllTest() {
        List<BikeDto> bikes = bikeService.getAll();
        assertTrue(bikes.size() > 4);
    }
}
