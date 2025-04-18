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
        Integer carId = carService.findAll().get(3).getId();
        Optional<CarDto> carDto = carService.findById(carId);
        assertTrue(carDto.isPresent(), "car с carId должен существовать");
        assertEquals("BMW", carService.findById(carId).get().getTitle());
        assertThrows(IllegalArgumentException.class, () -> {
            carService.findById(null);
        });
    }

    @Test
    @Description(value = "Тест проверяет обновление машины, а также соответствие полей после обновления")
    @DisplayName("Тест обновления машины")
    void updateTest() {
        Integer carId = carService.findAll().get(3).getId();
        CarDto carDto = CarDto.builder()
                .title("BMW")
                .price(540.5)
                .horsePower(450)
                .volume(5)
                .color("blue")
                .build();
        carService.update(carId, carDto);
        assertEquals(540.5, carService.findById(carId).get().getPrice());
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

        int carDtoId = carService.create(carDto).getId();
        assertEquals("Renault", carService.findById(carDtoId).get().getTitle());
        assertEquals(6, carService.findAll().size());

        Integer carId = carService.findAll().get(4).getId();
        assertTrue(carService.findById(carId).isPresent());
        assertEquals(6, carService.findAll().size());
        carService.delete(carId);
        Optional<CarDto> car = carService.findById(carId);
        assertTrue(car.isEmpty());
        assertEquals(5, carService.findAll().size());
    }

    @Test
    @Description(value = "Тест проверяет возвращение всех машин")
    @DisplayName("Тест возвращения всех машин")
    void getAllTest() {
        List<CarDto> cars = carService.findAll();
        assertTrue(cars.size() > 4);
    }
}
