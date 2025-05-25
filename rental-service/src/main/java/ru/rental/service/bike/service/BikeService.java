package ru.rental.service.bike.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rental.service.bike.dto.BikeDto;
import ru.rental.service.bike.dto.BikeDtoCreate;
import ru.rental.service.bike.entity.Bike;
import ru.rental.service.bike.repository.BikeRepository;
import ru.rental.service.ServiceInterface;
import ru.rental.service.ServiceInterfaceUserId;
import ru.rental.service.user.entity.User;
import ru.rental.service.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BikeService implements ServiceInterface<BikeDto, BikeDtoCreate>, ServiceInterfaceUserId<BikeDto> {

    private final BikeRepository bikeRepository;

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Optional<BikeDto> findById(Integer id) {
        return bikeRepository.findById(id)
                .map(BikeDto::toEntity);
    }

    @Transactional
    public BikeDto create(BikeDtoCreate bikeDtoCreate) {
        User user = userRepository.findById(bikeDtoCreate.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Bike bike = bikeDtoCreate.toEntity(user);
        Bike savedBike = bikeRepository.save(bike);

        return BikeDto.toEntity(savedBike);
    }

    @Transactional
    public BikeDto update(BikeDto updateBikeDto) {
        Bike existing = bikeRepository.findById(updateBikeDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Bike not found"));
        User user = userRepository.findById(updateBikeDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        existing.setName(updateBikeDto.getName());
        existing.setPrice(updateBikeDto.getPrice());
        existing.setHorsePower(updateBikeDto.getHorsePower());
        existing.setUser(user);

        Bike updatedBike = bikeRepository.save(existing);

        return BikeDto.toEntity(updatedBike);
    }

    @Transactional
    public boolean delete(Integer id) {
        if (bikeRepository.existsById(id)) {
            bikeRepository.deleteById(id);

            return true;
        }

        return false;
    }

    @Transactional(readOnly = true)
    public List<BikeDto> findByUserId(Integer userId) {
        return bikeRepository.findByUserId(userId).stream()
                .map(BikeDto::toEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BikeDto> getAll() {
        return ((List<Bike>) bikeRepository.findAll()).stream()
                .map(BikeDto::toEntity)
                .toList();
    }
}
