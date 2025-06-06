package ru.rental.service.car.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rental.service.car.MapperUtilCar;
import ru.rental.service.car.UserTemplate;
import ru.rental.service.car.entity.Car;
import ru.rental.service.car.repository.CarRepository;
import ru.rental.service.common.dto.CarDto;
import ru.rental.service.common.dto.CarDtoCreate;
import ru.rental.service.common.service.ServiceInterface;
import ru.rental.service.common.service.ServiceInterfaceUserId;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CarService implements ServiceInterface<CarDto, CarDtoCreate>, ServiceInterfaceUserId<CarDto> {

    private final CarRepository carRepository;

    private final UserTemplate userTemplate;

    private final MapperUtilCar mapperUtilCar;

    @Transactional(readOnly = true)
    public Optional<CarDto> findById(Integer id) {
        return carRepository.findById(id)
                .map(mapperUtilCar::toDto);
    }

    @Transactional
    public CarDto create(CarDtoCreate carDtoCreate) {

        Car car = mapperUtilCar.toEntity(carDtoCreate);
        Car savedCar = carRepository.save(car);

        return mapperUtilCar.toDto(savedCar);
    }

    @Transactional
    public CarDto update(CarDto updateCarDto) {
        Car existing = carRepository.findById(updateCarDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Car not found"));

        existing.setTitle(updateCarDto.getTitle());
        existing.setPrice(updateCarDto.getPrice());
        existing.setHorsePower(updateCarDto.getHorsePower());
        existing.setVolume(updateCarDto.getVolume());
        existing.setColor(updateCarDto.getColor());
        existing.setUserId(updateCarDto.getUserId());

        Car savedCar = carRepository.save(existing);

        return mapperUtilCar.toDto(savedCar);
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
                .map(mapperUtilCar::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CarDto> findByUserId(Integer userId) {
        return carRepository.findById(userId).stream()
                .map(mapperUtilCar::toDto)
                .toList();
    }
}
