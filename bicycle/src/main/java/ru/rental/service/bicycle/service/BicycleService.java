package ru.rental.service.bicycle.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rental.service.bicycle.MapperUtilBicycle;
import ru.rental.service.bicycle.UserTemplate;
import ru.rental.service.common.dto.BicycleDto;
import ru.rental.service.common.dto.BicycleDtoCreate;
import ru.rental.service.bicycle.entity.Bicycle;
import ru.rental.service.bicycle.repository.BicycleRepository;
import ru.rental.service.common.dto.UserDto;
import ru.rental.service.common.service.ServiceInterface;
import ru.rental.service.common.service.ServiceInterfaceUserId;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BicycleService implements ServiceInterface<BicycleDto, BicycleDtoCreate>, ServiceInterfaceUserId<BicycleDto> {

    private final BicycleRepository bicycleRepository;

    private final UserTemplate userTemplate;

    private final MapperUtilBicycle mapperUtilBicycle;

    @Transactional(readOnly = true)
    public Optional<BicycleDto> findById(Integer id) {
        return bicycleRepository.findById(id)
                .map(mapperUtilBicycle::toDto);
    }

    @Transactional
    public BicycleDto create(BicycleDtoCreate bicycleDtoCreate) {
        Bicycle bicycle = mapperUtilBicycle.toEntity(bicycleDtoCreate);
        Bicycle savedBicycle = bicycleRepository.save(bicycle);

        return mapperUtilBicycle.toDto(savedBicycle);
    }


    @Transactional
    public BicycleDto update(BicycleDto updateBicycleDto) {
        Bicycle existing = bicycleRepository.findById(updateBicycleDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Bicycle not found"));
        UserDto userDto = userTemplate.findById(updateBicycleDto.getUserId());

        existing.setModel(updateBicycleDto.getModel());
        existing.setPrice(updateBicycleDto.getPrice());
        existing.setColor(updateBicycleDto.getColor());
        existing.setUserId(userDto.getId());

        Bicycle updated = bicycleRepository.save(existing);

        return mapperUtilBicycle.toDto(updated);
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
                .map(mapperUtilBicycle::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BicycleDto> getAll() {
        return ((List<Bicycle>) bicycleRepository.findAll()).stream()
                .map(mapperUtilBicycle::toDto)
                .toList();
    }
}
