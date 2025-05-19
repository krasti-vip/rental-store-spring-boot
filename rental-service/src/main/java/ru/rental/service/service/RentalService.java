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
public class RentalService implements ServiceInterface<RentalDto, RentalDtoCreate>, ServiceInterfaceUserId<RentalDto> {

    private final RentalRepository rentalRepository;

    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public Optional<RentalDto> findById(Integer id) {
        return rentalRepository.findById(id)
                .map(this::convertToDto);
    }

    @Transactional
    public RentalDto create(RentalDtoCreate rentalDtoCreate) {
        Rental rental = modelMapper.map(rentalDtoCreate, Rental.class);
        Rental savedRental = rentalRepository.save(rental);

        return convertToDto(savedRental);
    }

    @Transactional
    public RentalDto update(RentalDto updateRentalDto) {
        Rental existingRental = rentalRepository.findById(updateRentalDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Rental not found"));
        modelMapper.map(updateRentalDto, existingRental);

        Rental savedRental = rentalRepository.save(existingRental);

        return convertToDto(savedRental);
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
                .map(this::convertToDto)
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
}
