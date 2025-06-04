package ru.rental.service.bike.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rental.service.bike.MapperUtilBike;
import ru.rental.service.bike.UserTemplate;
import ru.rental.service.bike.entity.Bike;
import ru.rental.service.bike.repository.BikeRepository;
import ru.rental.service.common.dto.BikeDto;
import ru.rental.service.common.dto.BikeDtoCreate;
import ru.rental.service.common.dto.UserDto;
import ru.rental.service.common.service.ServiceInterface;
import ru.rental.service.common.service.ServiceInterfaceUserId;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BikeService implements ServiceInterface<BikeDto, BikeDtoCreate>, ServiceInterfaceUserId<BikeDto> {

    private final BikeRepository bikeRepository;

    private final UserTemplate userTemplate;

    private final MapperUtilBike mapperUtilBike;

    @Transactional(readOnly = true)
    public Optional<BikeDto> findById(Integer id) {
        return bikeRepository.findById(id)
                .map(mapperUtilBike::toDto);
    }

    @Transactional
    public BikeDto create(BikeDtoCreate bikeDtoCreate) {
        Bike bike = mapperUtilBike.toEntity(bikeDtoCreate);
        Bike savedBike = bikeRepository.save(bike);

        return mapperUtilBike.toDto(savedBike);
    }

    @Transactional
    public BikeDto update(BikeDto updateBikeDto) {
        Bike existing = bikeRepository.findById(updateBikeDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Bike not found"));
        UserDto userDto = userTemplate.findById(updateBikeDto.getUserId());

        existing.setName(updateBikeDto.getName());
        existing.setPrice(updateBikeDto.getPrice());
        existing.setHorsePower(updateBikeDto.getHorsePower());
        existing.setUserId(userDto.getId());

        Bike updatedBike = bikeRepository.save(existing);

        return mapperUtilBike.toDto(updatedBike);
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
                .map(mapperUtilBike::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BikeDto> getAll() {
        return ((List<Bike>) bikeRepository.findAll()).stream()
                .map(mapperUtilBike::toDto)
                .toList();
    }
}
