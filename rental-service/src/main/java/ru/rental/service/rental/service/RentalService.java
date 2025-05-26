package ru.rental.service.rental.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rental.service.bicycle.entity.Bicycle;
import ru.rental.service.bicycle.repository.BicycleRepository;
import ru.rental.service.bike.entity.Bike;
import ru.rental.service.bike.repository.BikeRepository;
import ru.rental.service.car.entity.Car;
import ru.rental.service.car.repository.CarRepository;
import ru.rental.service.rental.dto.RentalDto;
import ru.rental.service.rental.dto.RentalDtoCreate;
import ru.rental.service.rental.entity.Rental;
import ru.rental.service.rental.repository.RentalRepository;
import ru.rental.service.ServiceInterface;
import ru.rental.service.ServiceInterfaceUserId;
import ru.rental.service.user.entity.User;
import ru.rental.service.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RentalService implements ServiceInterface<RentalDto, RentalDtoCreate>, ServiceInterfaceUserId<RentalDto> {

    private final RentalRepository rentalRepository;

    private final UserRepository userRepository;

    private final BikeRepository bikeRepository;

    private final BicycleRepository bicycleRepository;

    private final CarRepository carRepository;

    @Transactional(readOnly = true)
    public Optional<RentalDto> findById(Integer id) {
        return rentalRepository.findById(id)
                .map(RentalDto::toEntity);
    }

    @Transactional
    public RentalDto create(RentalDtoCreate rentalDtoCreate) {
        User user = userRepository.findById(rentalDtoCreate.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Bike bike = bikeRepository.findById(rentalDtoCreate.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Bike not found"));
        Car car = carRepository.findById(rentalDtoCreate.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Car not found"));
        Bicycle bicycle = bicycleRepository.findById(rentalDtoCreate.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Bicycle not found"));

        Rental rental = rentalDtoCreate.toEntity(user, car, bike, bicycle);
        Rental savedRental = rentalRepository.save(rental);

        return RentalDto.toEntity(savedRental);
    }

    @Transactional
    public RentalDto update(RentalDto updateRentalDto) {
        Rental existing = rentalRepository.findById(updateRentalDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Rental not found"));
        User user = userRepository.findById(updateRentalDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Bike bike = bikeRepository.findById(updateRentalDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Bike not found"));
        Car car = carRepository.findById(updateRentalDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Car not found"));
        Bicycle bicycle = bicycleRepository.findById(updateRentalDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Bicycle not found"));

        existing.setUser(user);
        existing.setBicycle(bicycle);
        existing.setCar(car);
        existing.setBike(bike);
        existing.setStartDate(updateRentalDto.getStartDate());
        existing.setEndDate(updateRentalDto.getEndDate());
        existing.setRentalAmount(updateRentalDto.getRentalAmount());
        existing.setIsPaid(updateRentalDto.getIsPaid());

        return RentalDto.toEntity(existing);
    }

    @Transactional
    public boolean delete(Integer id) {
        if (rentalRepository.existsById(id)) {
            rentalRepository.deleteById(id);

            return true;
        }

        return false;
    }

    @Transactional(readOnly = true)
    public List<RentalDto> findByUserId(Integer userId) {
        return rentalRepository.findByUserId(userId).stream()
                .map(RentalDto::toEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RentalDto> getAll() {
        return ((List<Rental>) rentalRepository.findAll()).stream()
                .map(RentalDto::toEntity)
                .toList();
    }
}
