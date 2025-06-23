package ru.rental.service.car.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import ru.rental.service.car.BaseBd;
import ru.rental.service.common.dto.CarDto;
import ru.rental.service.common.dto.CarDtoCreate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Тест CarService")
class CarServiceTest extends BaseBd {

    @Autowired
    private CarService carService;

    @Test
    @Description(value = "Тест проверяет существование машины, затем что возвращается правильная машина по айди")
    @DisplayName("Тест get машин")
    void findBiId() {
        assertTrue(carService.findById(2).isPresent());
        CarDto dto = carService.findById(2).get();
        assertEquals("HONDA", dto.getTitle());
    }

    @Test
    @Description(value = "Тест проверяет сохранение и удаление машины")
    @DisplayName("Тест create() и delete() для машины")
    void createAndDeleteTest() {
        CarDtoCreate carDtoCreeate = new CarDtoCreate(
                "HONDA",
                450.50,
                230,
                3.4,
                "black",
                null
        );

        assertEquals(5, carService.getAll().size());
        carService.create(carDtoCreeate);
        assertEquals(6, carService.getAll().size());
        assertEquals(450.50, carService.findById(6).get().getPrice());

        assertTrue(carService.delete(6));
        assertEquals(5, carService.getAll().size());
    }

    @Test
    @Description("Тест проверяет обновление машины")
    @DisplayName("Тест carUpdate() для машины")
    void carUpdateTest() {

        CarDto carDto = new CarDto(
                2,
                "HONDA",
                650.50,
                190,
                2.4,
                "red",
                2
        );

        assertEquals(360.50, carService.findById(2).get().getPrice());
        carService.update(carDto);
        assertEquals(650.50, carService.findById(2).get().getPrice());
        assertEquals(1, carService.findByUserId(1).size());
    }

    @Test
    @Description(value = "Тест проверяет получение всех машин")
    @DisplayName("Тест getAll() для машин")
    void getAllTest() {
        List<CarDto> car = carService.getAll();
        assertFalse(car.isEmpty());
        assertEquals(5, car.size());
    }
}
