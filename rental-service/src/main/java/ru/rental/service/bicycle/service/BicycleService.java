package ru.rental.service.bicycle.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rental.service.bicycle.dto.BicycleDto;
import ru.rental.service.bicycle.dto.BicycleDtoCreate;
import ru.rental.service.bicycle.entity.Bicycle;
import ru.rental.service.bicycle.repository.BicycleRepository;
import ru.rental.service.ServiceInterface;
import ru.rental.service.ServiceInterfaceUserId;
import ru.rental.service.user.entity.User;
import ru.rental.service.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BicycleService implements ServiceInterface<BicycleDto, BicycleDtoCreate>, ServiceInterfaceUserId<BicycleDto> {

    private final BicycleRepository bicycleRepository;

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Optional<BicycleDto> findById(Integer id) {
        return bicycleRepository.findById(id)
                .map(BicycleDto::toEntity);
    }

    @Transactional
    public BicycleDto create(BicycleDtoCreate bicycleDtoCreate) {
        User user = userRepository.findById(bicycleDtoCreate.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Bicycle bicycle = bicycleDtoCreate.toEntity(user);
        Bicycle savedBicycle = bicycleRepository.save(bicycle);

        return BicycleDto.toEntity(savedBicycle);
    }


    @Transactional
    public BicycleDto update(BicycleDto updateBicycleDto) {
        Bicycle existing = bicycleRepository.findById(updateBicycleDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Bicycle not found"));
        User user = userRepository.findById(updateBicycleDto.getUserId())
                        .orElseThrow(() -> new EntityNotFoundException("User not found"));

        existing.setModel(updateBicycleDto.getModel());
        existing.setPrice(updateBicycleDto.getPrice());
        existing.setColor(updateBicycleDto.getColor());
        existing.setUser(user);

        Bicycle updated = bicycleRepository.save(existing);

        return BicycleDto.toEntity(updated);
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
                .map(BicycleDto::toEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BicycleDto> getAll() {
        return ((List<Bicycle>) bicycleRepository.findAll()).stream()
                .map(BicycleDto::toEntity)
                .toList();
    }
}
