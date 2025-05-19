package ru.rental.service.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rental.service.dto.BikeDto;
import ru.rental.service.dto.create.BikeDtoCreeate;
import ru.rental.service.entity.Bike;
import ru.rental.service.repository.BikeRepository;
import ru.rental.service.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BikeService implements ServiceInterface<BikeDto, BikeDtoCreeate>, ServiceInterfaceUserId<BikeDto> {

    private final BikeRepository bikeRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public Optional<BikeDto> findById(Integer id) {
        return bikeRepository.findById(id)
                .map(this::convertToDtoUser);
    }

    @Transactional
    public BikeDto create(BikeDtoCreeate bikeDtoCreeate) {
        Bike bike = modelMapper.map(bikeDtoCreeate, Bike.class);
        Bike savedBike = bikeRepository.save(bike);

        return convertToDto(savedBike);
    }

    @Transactional
    public BikeDto update(BikeDto bikeDto) {
        Bike existingBike = bikeRepository.findById(bikeDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Bike not found"));
        modelMapper.map(bikeDto, existingBike);
        setUserIfExists(existingBike, bikeDto.getUserId());
        Bike savedBike = bikeRepository.save(existingBike);

        return convertToDto(savedBike);
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
    public List<BikeDto> getAll() {
        return ((List<Bike>) bikeRepository.findAll()).stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BikeDto> findByUserId(Integer userId) {
        return bikeRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .toList();
    }

    private BikeDto convertToDto(Bike bike) {
        return modelMapper.map(bike, BikeDto.class);
    }

    private BikeDto convertToDtoUser(Bike bike) {
        BikeDto dto = convertToDto(bike);
        if (bike.getUser() != null) {
            dto.setUserId(bike.getUser().getId());
        }

        return dto;
    }

    private void setUserIfExists(Bike bike, Integer userId) {
        userRepository.findById(userId).ifPresent(bike::setUser);
    }
}
