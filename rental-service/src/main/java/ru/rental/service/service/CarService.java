package ru.rental.service.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rental.service.dto.CarDto;
import ru.rental.service.entity.Car;
import ru.rental.service.repository.CarRepository;
import ru.rental.service.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public Optional<CarDto> findById(Integer id) {
        return carRepository.findById(id)
                .map(this::convertToDtoWithUser);
    }

    @Transactional
    public CarDto create(CarDto carDto) {
        Car car = modelMapper.map(carDto, Car.class);
        setUserIfExists(car, carDto.getUserId());
        Car savedCar = carRepository.save(car);
        return convertToDto(savedCar);
    }

    @Transactional
    public Optional<CarDto> update(Integer id, CarDto carDto) {
        return carRepository.findById(id)
                .map(existingCar -> {
                    updateCarFields(existingCar, carDto);
                    setUserIfExists(existingCar, carDto.getUserId());
                    Car updatedCar = carRepository.save(existingCar);
                    return convertToDtoWithUser(updatedCar);
                });
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
    public List<CarDto> findAll() {
        return ((List<Car>) carRepository.findAll()).stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CarDto> findByUserId(Integer userId) {
        return carRepository.findById(userId).stream()
                .map(this::convertToDto)
                .toList();
    }

    private CarDto convertToDto(Car car) {
        return modelMapper.map(car, CarDto.class);
    }

    private CarDto convertToDtoWithUser(Car car) {
        CarDto dto = convertToDto(car);
        if (car.getUser() != null) {
            dto.setUserId(car.getUser().getId());
        }
        return dto;
    }

    private void updateCarFields(Car car, CarDto dto) {
        modelMapper.map(dto, car);
    }

    private void setUserIfExists(Car car, Integer userId) {
        if (userId != null) {
            userRepository.findById(userId).ifPresent(car::setUser);
        }
    }
}
