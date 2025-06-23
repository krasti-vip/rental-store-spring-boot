package ru.rental.service.bike.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import ru.rental.service.bike.BaseBd;
import ru.rental.service.common.dto.BikeDto;
import ru.rental.service.common.dto.BikeDtoCreate;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Тест BikeService")
class BikeServiceTest extends BaseBd {

    @Autowired
    BikeService bikeService;

    @Test
    @Description(value = "Тест проверяет существование мотоцикла, затем что возвращается правильный мотоцикл по айди")
    @DisplayName("Тест получения мотоцикла по ID")
    void findBiId() {
        assertTrue(bikeService.findById(3).isPresent());
        BikeDto dto = bikeService.findById(3).get();
        assertEquals("YAMAHA", dto.getName());
    }

    @Test
    @Description(value = "Тест проверяет сохранение и удаление мотоцикла")
    @DisplayName("Тест create() и delete() для мотоцикла")
    void createAndDeleteTest() {
        BikeDtoCreate bikeDtoCreate = new BikeDtoCreate(
                "Harley",
                54.5,
                75,
                0.75,
                null
        );
        assertEquals(5, bikeService.getAll().size());
        bikeService.create(bikeDtoCreate);
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
                5,
                "YAMAHA",
                40000,
                400,
                1.0,
                null
        );

        assertEquals(3, bikeService.findByUserId(1).size());
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
