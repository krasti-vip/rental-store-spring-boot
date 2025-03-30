package ru.rental.service;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rental.service.dto.BikeDto;
import ru.rental.service.service.BikeService;

import java.util.List;
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
        Optional<BikeDto> bikeDto = bikeService.get(bikeId);
        assertTrue(bikeDto.isPresent(), "bike с bikeId должен существовать");
        assertEquals("URAL", bikeService.get(bikeId).get().getName());
        assertThrows(IllegalArgumentException.class, () -> {
            bikeService.get(null);
        });
    }

    @Test
    @Description(value = "Тест проверяет обновление мотоцикла, а также соответствие полей после обновления")
    @DisplayName("Тест обновление мотоцикла")
    void updateTest() {
        Integer bikeId = bikeService.getAll().get(3).getId();
        BikeDto bikeDto = BikeDto.builder()
                .name("URAL")
                .price(2000)
                .horsePower(200)
                .volume(0.75)
                .build();
        bikeService.update(bikeId, bikeDto);
        assertEquals(0.75, bikeService.get(bikeId).get().getVolume());
    }

    @Test
    @Description(value = "Тест проверяет сохранение мотоцикла, а затем его удаление и что количество мотоциклов сначала " +
                         "изменилось, а потом вернулась")
    @DisplayName("Тест сохранения, а затем удаление мотоцикла")
    void saveAndDeleteTest() {
        BikeDto bikeDto = BikeDto.builder()
                .name("Planet")
                .price(156)
                .horsePower(25)
                .volume(0.5)
                .build();

        int bikeDtoId = bikeService.save(bikeDto).getId();
        assertEquals("Planet", bikeService.get(bikeDtoId).get().getName());
        assertEquals(6, bikeService.getAll().size());

        Integer bikeId = bikeService.getAll().get(4).getId();
        assertTrue(bikeService.get(bikeId).isPresent());
        assertEquals(6, bikeService.getAll().size());
        bikeService.delete(bikeId);
        Optional<BikeDto> bike = bikeService.get(bikeId);
        assertTrue(bike.isEmpty());
        assertEquals(5, bikeService.getAll().size());
    }

    @Test
    @Description(value = "Тест проверяет возвращение всех мотоциклов")
    @DisplayName("Тест возвращения всех мотоциклов")
    void getAllTest() {
        List<BikeDto> bikes = bikeService.getAll();
        assertTrue(bikes.size() > 4);
    }

    @Test
    @Description(value = "Тест проверяет фильтрацию мотоциклов по предикату")
    @DisplayName("Тест фильтрации")
    void filterTest() {
        List<BikeDto> bikeFilter = bikeService.filterBy(u -> u.getName().equals("SUZUKI"));
        assertTrue(bikeFilter.size() > 0);
        assertEquals("SUZUKI", bikeFilter.get(0).getName());
    }

    @Test
    @Description(value = "Тест проверяет обновление списка аренды мотоциклов у пользователя и что определенный мотоцикл " +
                         "принадлежит определенному пользователю")
    @DisplayName("Тест обновления аренды мотоцикла")
    void updateUserIdTest() {
        Integer bikeId = bikeService.getAll().get(2).getId();
        Integer newUserId = 3;

        Optional<BikeDto> updatedBike = bikeService.updateUserId(bikeId, newUserId);

        assertNotNull(updatedBike.get().getUserId());
        System.out.println(updatedBike.get().getUserId());

        assertTrue(updatedBike.isPresent(), "Обновленный bike должен быть не null");
        assertEquals(newUserId, updatedBike.get().getUserId(), "userId bike должен обновиться");

        BikeDto actualBike = bikeService.get(bikeId).orElseThrow(() -> new IllegalStateException("Bike not found"));
        assertEquals(newUserId, actualBike.getUserId(), "userId в базе данных должен совпадать");

        List<BikeDto> userBike = bikeService.getAllByUserId(newUserId);

        assertNotNull(userBike, "Список bikes пользователя не должен быть null");
        assertFalse(userBike.isEmpty(), "Список bikes пользователя не должен быть пустым");

        for (BikeDto bike : userBike) {
            assertEquals(newUserId, bike.getUserId(), "Все bikes должны принадлежать пользователю с ID " + newUserId);
        }
    }
}
