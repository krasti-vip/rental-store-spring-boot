package ru.rental.service;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rental.service.entity.Car;
import ru.rental.service.repository.CarRepository;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Тест CarDao")
class CarEntityTest extends BaseBd {

    @Autowired
    private CarRepository carRepository;

    private static Stream<Arguments> sourceCar() {
        return Stream.of(
                Arguments.of(
                        Car.builder()
                                .id(1)
                                .title("MERCEDES")
                                .price(655.3)
                                .horsePower(250)
                                .volume(3.5)
                                .color("black")
                                .build()
                ),
                Arguments.of(
                        Car.builder()
                                .id(2)
                                .title("HONDA")
                                .price(360.5)
                                .horsePower(190)
                                .volume(2.4)
                                .color("red")
                                .build()
                ),
                Arguments.of(
                        Car.builder()
                                .id(3)
                                .title("HYUNDAI")
                                .price(320.9)
                                .horsePower(156)
                                .volume(2.)
                                .color("white")
                                .build()
                ),
                Arguments.of(
                        Car.builder()
                                .id(4)
                                .title("BMW")
                                .price(640.5)
                                .horsePower(450)
                                .volume(5.)
                                .color("blue")
                                .build()
                ),
                Arguments.of(
                        Car.builder()
                                .id(5)
                                .title("OPEL")
                                .price(210.9)
                                .horsePower(110)
                                .volume(1.8)
                                .color("gold")
                                .build()
                )
        );
    }

    @Test
    @Description(value = "Тест проверяет возвращения всех машин")
    @DisplayName("Тест возвращения всех машин")
    void carDaoGetAllTest() {
        List<Car> cars = (List<Car>) carRepository.findAll();
        assertEquals(5, cars.size());
    }

    @ParameterizedTest
    @MethodSource("sourceCar")
    @Description(value = "Тест проверяет возвращения машин и что у них правильные поля")
    @DisplayName("Тест getCar")
    void getCarTest(Car sourceCar) {
        final var car = carRepository.findById(sourceCar.getId());

        assertAll(
                () -> assertEquals(car.get().getId(), sourceCar.getId()),
                () -> assertEquals(car.get().getTitle(), sourceCar.getTitle()),
                () -> assertEquals(car.get().getPrice(), sourceCar.getPrice()),
                () -> assertEquals(car.get().getVolume(), sourceCar.getVolume()),
                () -> assertEquals(car.get().getHorsePower(), sourceCar.getHorsePower()),
                () -> assertEquals(car.get().getColor(), sourceCar.getColor())
        );
    }

    @ParameterizedTest
    @MethodSource("sourceCar")
    @Description(value = "Тест проверяет обновление машин, а так же все их поля на соответствие изменений")
    @DisplayName("Тест обновления машины")
    void updateCarTest(Car sourceCar) {

        Car updatedCar = new Car(
                sourceCar.getId(),
                sourceCar.getTitle(),
                sourceCar.getPrice() + 1000,
                sourceCar.getHorsePower() + 20,
                sourceCar.getVolume(),
                sourceCar.getColor(),
                sourceCar.getUser()
        );

        Car carUpdate = carRepository.save(updatedCar);
        assertAll(
                () -> assertEquals(carUpdate.getId(), updatedCar.getId()),
                () -> assertEquals(carUpdate.getTitle(), updatedCar.getTitle()),
                () -> assertEquals(carUpdate.getPrice(), updatedCar.getPrice()),
                () -> assertEquals(carUpdate.getHorsePower(), updatedCar.getHorsePower()),
                () -> assertEquals(carUpdate.getVolume(), updatedCar.getVolume()),
                () -> assertEquals(carUpdate.getColor(), updatedCar.getColor())
        );
    }

    @ParameterizedTest
    @MethodSource("sourceCar")
    @Description(value = "Тест проверяет сохранение машины, а затем его удаление и что количество машин сначала " +
                         "изменилось, а потом вернулась")
    @DisplayName("Тест сохранения и удаление машины")
    void saveAndDeleteCarTest(Car sourceCar) {
        Car carToSave = new Car(
                6,
                sourceCar.getTitle(),
                sourceCar.getPrice(),
                sourceCar.getHorsePower(),
                sourceCar.getVolume(),
                sourceCar.getColor(),
                sourceCar.getUser()
        );

        Car savedCar = carRepository.save(carToSave);
        assertNotNull(savedCar, "Car должен быть успешно сохранен");

        List<Car> cars = (List<Car>) carRepository.findAll();

        assertEquals(6, cars.size(), "После сохранения в базе должно быть 6 car");
        assertTrue(carRepository.existsById(savedCar.getId()), "Сохраненный car должен быть в списке");

        Car savedCarFromDb = carRepository.findById(savedCar.getId())
                        .orElseThrow(() -> new RuntimeException("<UNK> <UNK> <UNK> <UNK> <UNK>"));
        assertAll(
                () -> assertEquals(carToSave.getTitle(), savedCarFromDb.getTitle(), "Имя car должно совпадать"),
                () -> assertEquals(carToSave.getPrice(), savedCarFromDb.getPrice(), "Цена car должна совпадать"),
                () -> assertEquals(carToSave.getHorsePower(), savedCarFromDb.getHorsePower(), "Мощность car должна совпадать"),
                () -> assertEquals(carToSave.getVolume(), savedCarFromDb.getVolume(), "Объем car должен совпадать"),
                () -> assertEquals(carToSave.getColor(), savedCarFromDb.getColor(), "Цвет car должен совпадать")
        );

        carRepository.deleteById(savedCar.getId());
        assertFalse(carRepository.existsById(savedCar.getId()));

        List<Car> carsFromDb = (List<Car>) carRepository.findAll();
        assertEquals(5, carsFromDb.size(), "После удаления car в базе должно быть 5 cars");
    }
}
