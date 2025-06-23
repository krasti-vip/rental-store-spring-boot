package ru.rental.service.rental.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rental.service.common.dto.*;
import ru.rental.service.rental.*;
import ru.rental.service.rental.entity.Rental;
import ru.rental.service.rental.repository.RentalRepository;
import ru.rental.service.common.service.ServiceInterface;
import ru.rental.service.common.service.ServiceInterfaceUserId;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RentalService implements ServiceInterface<RentalDto, RentalDtoCreate>, ServiceInterfaceUserId<RentalDto> {

    private final RentalRepository rentalRepository;

    private final UserTemplate userTemplate;

    private final BikeTemplate bikeTemplate;

    private final BicycleTemplate bicycleTemplate;

    private final CarTemplate carTemplate;

    private final MapperUtilRental mapperUtilRental;

    @Transactional(readOnly = true)
    public Optional<RentalDto> findById(Integer id) {
        return rentalRepository.findById(id)
                .map(mapperUtilRental::toDto);
    }

    @Transactional
    public RentalDto create(RentalDtoCreate rentalDtoCreate) {

        Integer userId = null;
        if (rentalDtoCreate.getUserId() != null) {
            userId = userTemplate.findById(rentalDtoCreate.getUserId()).getId();
        }

        Integer bikeId = null;
        if (rentalDtoCreate.getBikeId() != null) {
            bikeId = bikeTemplate.findById(rentalDtoCreate.getBikeId()).getId();
        }

        Integer carId = null;
        if (rentalDtoCreate.getCarId() != null) {
            carId = carTemplate.findById(rentalDtoCreate.getCarId()).getId();
        }

        Integer bicycleId = null;
        if (rentalDtoCreate.getBicycleId() != null) {
            bicycleId = bicycleTemplate.findById(rentalDtoCreate.getBicycleId()).getId();
        }

        Rental rental = mapperUtilRental.toEntity(rentalDtoCreate, userId, bikeId, carId, bicycleId);
        Rental savedRental = rentalRepository.save(rental);

        return mapperUtilRental.toDto(savedRental);
    }

    @Transactional
    public RentalDto update(RentalDto updateRentalDto) {
        Rental existing = rentalRepository.findById(updateRentalDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Rental not found"));

        Optional.ofNullable(updateRentalDto.getUserId());
        Optional.ofNullable(updateRentalDto.getBicycleId());
        Optional.ofNullable(updateRentalDto.getCarId());
        Optional.ofNullable(updateRentalDto.getBikeId());

        Optional.ofNullable(updateRentalDto.getStartDate()).ifPresent(existing::setStartDate);
        Optional.ofNullable(updateRentalDto.getEndDate()).ifPresent(existing::setEndDate);
        Optional.ofNullable(updateRentalDto.getRentalAmount()).ifPresent(existing::setRentalAmount);
        Optional.ofNullable(updateRentalDto.getIsPaid()).ifPresent(existing::setIsPaid);

        return mapperUtilRental.toDto(rentalRepository.save(existing));
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
                .map(mapperUtilRental::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RentalDto> getAll() {
        return ((List<Rental>) rentalRepository.findAll()).stream()
                .map(mapperUtilRental::toDto)
                .toList();
    }
}
