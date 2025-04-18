package ru.rental.service;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rental.service.dto.CarDto;
import ru.rental.service.service.CarService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Тест CarService")
class CarServiceTest extends BaseBd {

    @Autowired
    private CarService carService;

    @Test
    @Description(value = "Тест проверяет существование машины, затем что возвращается правильная машина по айди")
    @DisplayName("Тест get")
    void getTest() {
        Integer carId = carService.getAll().get(3).getId();
        Optional<CarDto> carDto = carService.get(carId);
        assertTrue(carDto.isPresent(), "car с carId должен существовать");
        assertEquals("BMW", carService.get(carId).get().getTitle());
        assertThrows(IllegalArgumentException.class, () -> {
            carService.get(null);
        });
    }

    @Test
    @Description(value = "Тест проверяет обновление машины, а также соответствие полей после обновления")
    @DisplayName("Тест обновления машины")
    void updateTest() {
        Integer carId = carService.getAll().get(3).getId();
        CarDto carDto = CarDto.builder()
                .title("BMW")
                .price(540.5)
                .horsePower(450)
                .volume(5)
                .color("blue")
                .build();
        carService.update(carId, carDto);
        assertEquals(540.5, carService.get(carId).get().getPrice());
    }

    @Test
    @Description(value = "Тест проверяет сохранение машины, а затем ее удаление и что количество машин сначала " +
                         "изменилось, а потом вернулась")
    @DisplayName("Тест сохранения и удаления")
    void saveAndDeleteTest() {
        CarDto carDto = CarDto.builder()
                .title("Renault")
                .price(120)
                .horsePower(75)
                .volume(1.5)
                .color("white")
                .build();

        int carDtoId = carService.save(carDto).getId();
        assertEquals("Renault", carService.get(carDtoId).get().getTitle());
        assertEquals(6, carService.getAll().size());

        Integer carId = carService.getAll().get(4).getId();
        assertTrue(carService.get(carId).isPresent());
        assertEquals(6, carService.getAll().size());
        carService.delete(carId);
        Optional<CarDto> car = carService.get(carId);
        assertTrue(car.isEmpty());
        assertEquals(5, carService.getAll().size());
    }

    @Test
    @Description(value = "Тест проверяет возвращение всех машин")
    @DisplayName("Тест возвращения всех машин")
    void getAllTest() {
        List<CarDto> cars = carService.getAll();
        assertTrue(cars.size() > 4);
    }

    @Test
    @Description(value = "Тест проверяет фильтрацию машин по предикату")
    @DisplayName("Тест фильтрации")
    void filterTest() {
        List<CarDto> carFilter = carService.filterBy(u -> u.getTitle().equals("HYUNDAI"));
        assertTrue(carFilter.size() > 0);
        assertEquals("HYUNDAI", carFilter.get(0).getTitle());
    }

    @Test
    @Description(value = "Тест проверяет обновление списка аренды машин у пользователя и что определенная машина " +
                         "принадлежит определенному пользователю")
    @DisplayName("Тест обновления аренды машины")
    void updateUserIdTest() {
        Integer carId = carService.getAll().get(2).getId();
        Integer newUserId = 3;

        Optional<CarDto> updatedCar = carService.updateUserId(carId, newUserId);

        assertNotNull(updatedCar.get().getUserId());
        System.out.println(updatedCar.get().getUserId());

        assertTrue(updatedCar.isPresent(), "Обновленная машина должна быть не null");
        assertEquals(newUserId, updatedCar.get().getUserId(), "userId машины должен обновиться");

        CarDto actualCar = carService.get(carId).orElseThrow(() -> new IllegalStateException("Car not found"));
        assertEquals(newUserId, actualCar.getUserId(), "userId в базе данных должен совпадать");

        List<CarDto> userCars = carService.getAllByUserId(newUserId);

        assertNotNull(userCars, "Список машин пользователя не должен быть null");
        assertFalse(userCars.isEmpty(), "Список машин пользователя не должен быть пустым");

        for (CarDto car : userCars) {
            assertEquals(newUserId, car.getUserId(), "Все машины должны принадлежать пользователю с ID " + newUserId);
        }
    }
}
