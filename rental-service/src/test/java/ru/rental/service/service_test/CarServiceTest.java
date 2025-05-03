package ru.rental.service.service_test;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rental.service.BaseBd;
import ru.rental.service.dto.CarDto;
import ru.rental.service.service.CarService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        Optional<CarDto> carDto = carService.findById(carId);
        assertTrue(carDto.isPresent(), "car с carId должен существовать");
        assertEquals("BMW", carService.findById(carId).get().getTitle());
    }

    @Test
    @Description("Тест проверяет частичное обновление машины через Map")
    @DisplayName("Тест update() для частичного обновления машины")
    void carUpdateTest() {

    }

    @Test
    @Description(value = "Тест проверяет сохранение машины, а затем ее удаление и что количество машин сначала " +
                         "изменилось, а потом вернулась")
    @DisplayName("Тест сохранения и удаления")
    void saveAndDeleteTest() {

    }

    @Test
    @Description(value = "Тест проверяет возвращение всех машин")
    @DisplayName("Тест возвращения всех машин")
    void getAllTest() {
        List<CarDto> cars = carService.getAll();
        assertTrue(cars.size() > 4);
    }
}
