package ru.rental.service.rental.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rental.service.common.dto.RentalDto;
import ru.rental.service.common.dto.RentalDtoCreate;
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
        Integer userId = userTemplate.findById(rentalDtoCreate.getUserId()).getId();
        Integer bikeId = bikeTemplate.findById(rentalDtoCreate.getUserId()).getId();
        Integer carId = carTemplate.findById(rentalDtoCreate.getUserId()).getId();
        Integer bicycleId = bicycleTemplate.findById(rentalDtoCreate.getUserId()).getId();

        Rental rental = mapperUtilRental.toEntity(rentalDtoCreate, userId, carId, bikeId, bicycleId);
        Rental savedRental = rentalRepository.save(rental);

        return mapperUtilRental.toDto(savedRental);
    }

    @Transactional
    public RentalDto update(RentalDto updateRentalDto) {
        Rental existing = rentalRepository.findById(updateRentalDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Rental not found"));

        existing.setUserId(updateRentalDto.getUserId());
        existing.setBicycleId(updateRentalDto.getBicycleId());
        existing.setCarId(updateRentalDto.getCarId());
        existing.setBikeId(updateRentalDto.getBikeId());
        existing.setStartDate(updateRentalDto.getStartDate());
        existing.setEndDate(updateRentalDto.getEndDate());
        existing.setRentalAmount(updateRentalDto.getRentalAmount());
        existing.setIsPaid(updateRentalDto.getIsPaid());

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
