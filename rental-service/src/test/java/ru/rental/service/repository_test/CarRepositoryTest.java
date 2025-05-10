package ru.rental.service.repository_test;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rental.service.BaseBd;
import ru.rental.service.entity.Car;
import ru.rental.service.repository.CarRepository;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CarRepositoryTest extends BaseBd {

    @Autowired
    private CarRepository carRepository;

    @Test
    @DisplayName("Тест получения всех машин")
    void findAllTest() {
        List<Car> cars = (List<Car>) carRepository.findAll();
        assertEquals(5, cars.size());
    }

    @Test
    @Transactional
    @DisplayName("Тест сохранения и удаления новой машины")
    void saveAndDeleteTest() {
        Car car = new Car(
                6,
                "Honda",
                423.56,
                149,
                2.5,
                "Gold",
                null
        );
        carRepository.save(car);
        List<Car> cars = (List<Car>) carRepository.findAll();
        assertEquals(6, cars.size());

        carRepository.deleteById(6);
        cars = (List<Car>) carRepository.findAll();
        assertEquals(5, cars.size());
    }

    @Test
    @DisplayName("Тест возвращения машины по id")
    void findByIdTest() {
        Car car = carRepository.findById(3).orElseThrow();
        assertEquals(320.90, car.getPrice());
    }

    @Test
    @DisplayName("Тест обновление машины")
    void updateCarTest() {
        Car car = carRepository.findById(1).orElseThrow();
        car.setPrice(39956.56);
        carRepository.save(car);
        assertEquals(car, carRepository.findById(1).get());
    }

    @Test
    @DisplayName("Тест получение машин User")
    void findCarByUserTest() {
        List<Car> cars = carRepository.findAllByUserId(1);
        assertEquals(1, cars.size());
    }
}
