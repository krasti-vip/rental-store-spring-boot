package ru.rental.service.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rental.service.dto.BicycleDto;
import ru.rental.service.entity.Bicycle;
import ru.rental.service.repository.BicycleRepository;
import ru.rental.service.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BicycleService {

    private final BicycleRepository bicycleRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public Optional<BicycleDto> findById(Integer id) {
        return bicycleRepository.findById(id)
                .map(this::convertToDtoWithUser);
    }

    @Transactional
    public BicycleDto create(BicycleDto bicycleDto) {
        Bicycle bicycle = modelMapper.map(bicycleDto, Bicycle.class);
        setUserIfExists(bicycle, bicycleDto.getUserId());
        Bicycle savedBicycle = bicycleRepository.save(bicycle);
        return convertToDto(savedBicycle);
    }

    @Transactional
    public Optional<BicycleDto> update(Integer id, BicycleDto bicycleDto) {
        return bicycleRepository.findById(id)
                .map(existingBicycle -> {
                    updateBicycleFields(existingBicycle, bicycleDto);
                    setUserIfExists(existingBicycle, bicycleDto.getUserId());
                    Bicycle updatedBicycle = bicycleRepository.save(existingBicycle);
                    return convertToDtoWithUser(updatedBicycle);
                });
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

    private void updateBicycleFields(Bicycle bicycle, BicycleDto dto) {
        modelMapper.map(dto, bicycle);
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
