package ru.rental.service.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rental.service.dto.RentalDto;
import ru.rental.service.entity.Rental;
import ru.rental.service.repository.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RentalService {

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
    public RentalDto create(RentalDto rentalDto) {
        Rental rental = modelMapper.map(rentalDto, Rental.class);
        setRelations(rental, rentalDto);
        Rental savedRental = rentalRepository.save(rental);
        return convertToDtoWithRelations(savedRental);
    }

    @Transactional
    public Optional<RentalDto> update(Integer id, RentalDto rentalDto) {
        return rentalRepository.findById(id)
                .map(existingRental -> {
                    updateRentalFields(existingRental, rentalDto);
                    setRelations(existingRental, rentalDto);
                    Rental updatedRental = rentalRepository.save(existingRental);
                    return convertToDtoWithRelations(updatedRental);
                });
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
    public List<RentalDto> findAll() {
        return ((List<Rental>) rentalRepository.findAll()).stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RentalDto> findActiveRentals() {
        return rentalRepository.findByEndDateIsNullOrEndDateAfter(LocalDateTime.now()).stream()
                .map(this::convertToDtoWithRelations)
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

    private void updateRentalFields(Rental rental, RentalDto dto) {
        modelMapper.map(dto, rental);
    }

    private void setRelations(Rental rental, RentalDto dto) {
        if (dto.getUserId() != null) {
            userRepository.findById(dto.getUserId()).ifPresent(rental::setUser);
        }
        if (dto.getCarId() != null) {
            carRepository.findById(dto.getCarId()).ifPresent(rental::setCar);
        }
        if (dto.getBikeId() != null) {
            bikeRepository.findById(dto.getBikeId()).ifPresent(rental::setBike);
        }
        if (dto.getBicycleId() != null) {
            bicycleRepository.findById(dto.getBicycleId()).ifPresent(rental::setBicycle);
        }
    }
}
