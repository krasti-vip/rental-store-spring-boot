package ru.rental.service.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rental.service.dto.BicycleDto;
import ru.rental.service.dto.create.BicycleDtoCreate;
import ru.rental.service.entity.Bicycle;
import ru.rental.service.entity.Bike;
import ru.rental.service.repository.BicycleRepository;
import ru.rental.service.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BicycleService implements ServiceInterface<BicycleDto, BicycleDtoCreate>, ServiceInterfaceUserId<BicycleDto> {

    private final BicycleRepository bicycleRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public Optional<BicycleDto> findById(Integer id) {
        return bicycleRepository.findById(id)
                .map(this::convertToDtoUser);
    }

    @Transactional
    public BicycleDto create(BicycleDtoCreate bicycleDtoCreate) {
        Bicycle bicycle = modelMapper.map(bicycleDtoCreate, Bicycle.class);
        setUserIfExists(bicycle, bicycleDtoCreate.getUserId());
        Bicycle savedBicycle = bicycleRepository.save(bicycle);
        return convertToDto(savedBicycle);
    }

    @Transactional
    public BicycleDto update(BicycleDto bicycleDto) {
        Bicycle existingBicycle = bicycleRepository.findById(bicycleDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Bicycle not found"));
        modelMapper.map(bicycleDto, existingBicycle);
        setUserIfExists(existingBicycle, bicycleDto.getUserId());
        Bicycle savedBicycle = bicycleRepository.save(existingBicycle);

        return convertToDto(savedBicycle);
    }

    @Transactional
    public boolean delete(Integer id) {
        if (bicycleRepository.existsById(id)) {
            bicycleRepository.deleteById(id);

            return true;
        }

        return false;
    }

    @Transactional(readOnly = true)
    public List<BicycleDto> findByUserId(Integer userId) {
        return bicycleRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BicycleDto> getAll() {
        return ((List<Bicycle>) bicycleRepository.findAll()).stream()
                .map(this::convertToDto)
                .toList();
    }

    private BicycleDto convertToDto(Bicycle bicycle) {
        return modelMapper.map(bicycle, BicycleDto.class);
    }

    private BicycleDto convertToDtoUser(Bicycle bicycle) {
        BicycleDto dto = convertToDto(bicycle);
        if (bicycle.getUser() != null) {
            dto.setUserId(bicycle.getUser().getId());
        }

        return dto;
    }

    private void setUserIfExists(Bicycle bicycle, Integer userId) {
        if (userId != null) {
            userRepository.findById(userId).ifPresent(bicycle::setUser);
        }
    }
}
