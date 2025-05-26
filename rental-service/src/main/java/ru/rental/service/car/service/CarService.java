package ru.rental.service.car.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rental.service.car.dto.CarDto;
import ru.rental.service.car.dto.CarDtoCreate;
import ru.rental.service.car.entity.Car;
import ru.rental.service.car.repository.CarRepository;
import ru.rental.service.ServiceInterface;
import ru.rental.service.ServiceInterfaceUserId;
import ru.rental.service.user.entity.User;
import ru.rental.service.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CarService implements ServiceInterface<CarDto, CarDtoCreate>, ServiceInterfaceUserId<CarDto> {

    private final CarRepository carRepository;

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Optional<CarDto> findById(Integer id) {
        return carRepository.findById(id)
                .map(CarDto::toEntity);
    }

    @Transactional
    public CarDto create(CarDtoCreate carDtoCreate) {
        User user = userRepository.findById(carDtoCreate.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Car car = carDtoCreate.toEntity(user);
        Car savedCar = carRepository.save(car);

        return CarDto.toEntity(savedCar);
    }

    @Transactional
    public CarDto update(CarDto updateCarDto) {
        Car existing = carRepository.findById(updateCarDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Car not found"));
        User user = userRepository.findById(updateCarDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        existing.setTitle(updateCarDto.getTitle());
        existing.setPrice(updateCarDto.getPrice());
        existing.setHorsePower(updateCarDto.getHorsePower());
        existing.setVolume(updateCarDto.getVolume());
        existing.setColor(updateCarDto.getColor());
        existing.setUser(user);

        Car savedCar = carRepository.save(existing);

        return CarDto.toEntity(savedCar);
    }

    @Transactional
    public boolean delete(Integer id) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public List<CarDto> getAll() {
        return ((List<Car>) carRepository.findAll()).stream()
                .map(CarDto::toEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CarDto> findByUserId(Integer userId) {
        return carRepository.findById(userId).stream()
                .map(CarDto::toEntity)
                .toList();
    }
}
