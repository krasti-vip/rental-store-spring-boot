package ru.rental.service.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rental.service.dto.BicycleDto;
import ru.rental.service.dto.create.BicycleDtoCreate;
import ru.rental.service.entity.Bicycle;
import ru.rental.service.repository.BicycleRepository;
import ru.rental.service.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BicycleService implements ServiceInterface<BicycleDto, BicycleDtoCreate>{

    private final BicycleRepository bicycleRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public Optional<BicycleDto> findById(Integer id) {
        return bicycleRepository.findById(id)
                .map(this::convertToDtoWithUser);
    }

    @Transactional
    public BicycleDto create(BicycleDtoCreate bicycleDtoCreate) {
        Bicycle bicycle = modelMapper.map(bicycleDtoCreate, Bicycle.class);
        setUserIfExists(bicycle, bicycleDtoCreate.getUserId());
        Bicycle savedBicycle = bicycleRepository.save(bicycle);
        return convertToDto(savedBicycle);
    }

    @Transactional
    public BicycleDto update(BicycleDto updateBicycleDto) {
        Bicycle existingBicycle = bicycleRepository.findById(updateBicycleDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Bicycle not found"));
        modelMapper.map(updateBicycleDto, existingBicycle);
        setUserIfExists(existingBicycle, updateBicycleDto.getUserId());
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
    public List<BicycleDto> findAll() {
        return ((List<Bicycle>) bicycleRepository.findAll()).stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BicycleDto> findByUserId(Integer userId) {
        return bicycleRepository.findById(userId).stream()
                .map(this::convertToDto)
                .toList();
    }

    private BicycleDto convertToDto(Bicycle bicycle) {
        return modelMapper.map(bicycle, BicycleDto.class);
    }

    private BicycleDto convertToDtoWithUser(Bicycle bicycle) {
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

    @Transactional(readOnly = true)
    public List<BicycleDto> getAll() {
        Iterable<Bicycle> bicycleDto = bicycleRepository.findAll();
        List<Bicycle> bicycleList = new ArrayList<>();
        bicycleDto.forEach(bicycleList::add);

        return bicycleList.stream()
                .map(this::convertToDtoWithUser)
                .toList();
    }
}
