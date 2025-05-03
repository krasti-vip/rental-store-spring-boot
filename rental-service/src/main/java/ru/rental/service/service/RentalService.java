package ru.rental.service.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rental.service.dto.RentalDto;
import ru.rental.service.dto.create.RentalDtoCreate;
import ru.rental.service.entity.*;
import ru.rental.service.repository.*;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RentalService implements ServiceInterface<RentalDto, RentalDtoCreate> {

    private final RentalRepository rentalRepository;

    private final UserRepository userRepository;

    private final CarRepository carRepository;

    private final BikeRepository bikeRepository;

    private final BicycleRepository bicycleRepository;

    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public Optional<RentalDto> findById(Integer id) {
        return rentalRepository.findById(id)
                .map(this::convertToDtoWithRelations);
    }

    @Transactional
    public RentalDto create(RentalDtoCreate rentalDtoCreate) {
        Rental rental = modelMapper.map(rentalDtoCreate, Rental.class);
        setUserIfExists(rental, rentalDtoCreate.getUserId());
        Rental savedRental = rentalRepository.save(rental);

        return convertToDtoWithRelations(savedRental);
    }

    @Transactional
    public RentalDto update(RentalDto updateRentalDto) {
        Rental existingRental = rentalRepository.findById(updateRentalDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Rental not found"));
        modelMapper.map(updateRentalDto, existingRental);
        setUserIfExists(existingRental, updateRentalDto.getUserId());
        setCarIfExists(existingRental, updateRentalDto.getCarId());
        setBikeIfExists(existingRental, updateRentalDto.getBikeId());
        setBicycleIfExists(existingRental, updateRentalDto.getBicycleId());
        Rental savedRental = rentalRepository.save(existingRental);

        return convertToDtoWithRelations(savedRental);
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
        return rentalRepository.findById(userId).stream()
                .map(this::convertToDtoWithRelations)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RentalDto> getAll() {
        return ((List<Rental>) rentalRepository.findAll()).stream()
                .map(this::convertToDto)
                .toList();
    }

    private RentalDto convertToDto(Rental rental) {
        return modelMapper.map(rental, RentalDto.class);
    }

    private RentalDto convertToDtoWithRelations(Rental rental) {
        RentalDto dto = convertToDto(rental);
        if (rental.getUser() != null) {
            dto.setUserId(rental.getUser().getId());
        }
        if (rental.getCar() != null) {
            dto.setCarId(rental.getCar().getId());
        }
        if (rental.getBike() != null) {
            dto.setBikeId(rental.getBike().getId());
        }
        if (rental.getBicycle() != null) {
            dto.setBicycleId(rental.getBicycle().getId());
        }
        return dto;
    }

    private void setBicycleIfExists(Rental rental, Integer bicycleId) {
        if (bicycleId != null) {
            bicycleRepository.findById(bicycleId).ifPresent(rental::setBicycle);
        }
    }

    private void setUserIfExists(Rental rental, Integer userId) {
        if (userId != null) {
            userRepository.findById(userId).ifPresent(rental::setUser);
        }
    }

    private void setCarIfExists(Rental rental, Integer carId) {
        if (carId != null) {
            carRepository.findById(carId).ifPresent(rental::setCar);
        }
    }

    private void setBikeIfExists(Rental rental, Integer bikeId) {
        if (bikeId != null) {
            bikeRepository.findById(bikeId).ifPresent(rental::setBike);
        }
    }
}
