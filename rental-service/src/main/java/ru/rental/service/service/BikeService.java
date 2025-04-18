package ru.rental.service.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rental.service.dto.BikeDto;
import ru.rental.service.entity.Bike;
import ru.rental.service.repository.BikeRepository;
import ru.rental.service.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BikeService {

    private final BikeRepository bikeRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public Optional<BikeDto> findById(Integer id) {
        return bikeRepository.findById(id)
                .map(this::convertToDtoWithUser);
    }

    @Transactional
    public BikeDto create(BikeDto bikeDto) {
        Bike bike = modelMapper.map(bikeDto, Bike.class);
        setUserIfExists(bike, bikeDto.getUserId());
        Bike savedBike = bikeRepository.save(bike);
        return convertToDto(savedBike);
    }

    @Transactional
    public Optional<BikeDto> update(Integer id, BikeDto bikeDto) {
        return bikeRepository.findById(id)
                .map(existingBike -> {
                    updateBikeFields(existingBike, bikeDto);
                    setUserIfExists(existingBike, bikeDto.getUserId());
                    Bike updatedBike = bikeRepository.save(existingBike);
                    return convertToDtoWithUser(updatedBike);
                });
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
    public List<BikeDto> findAll() {
        return ((List<Bike>) bikeRepository.findAll()).stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BikeDto> findByUserId(Integer userId) {
        return bikeRepository.findById(userId).stream()
                .map(this::convertToDto)
                .toList();
    }

    private BikeDto convertToDto(Bike bike) {
        return modelMapper.map(bike, BikeDto.class);
    }

    private BikeDto convertToDtoWithUser(Bike bike) {
        BikeDto dto = convertToDto(bike);
        if (bike.getUser() != null) {
            dto.setUserId(bike.getUser().getId());
        }
        return dto;
    }

    private void updateBikeFields(Bike bike, BikeDto dto) {
        modelMapper.map(dto, bike);
    }

    private void setUserIfExists(Bike bike, Integer userId) {
        if (userId != null) {
            userRepository.findById(userId).ifPresent(bike::setUser);
        }
    }

    @Transactional(readOnly = true)
    public List<BikeDto> getAll() {
        Iterable<Bike> bikes = bikeRepository.findAll();
        List<Bike> bikeList = new ArrayList<>();
        bikes.forEach(bikeList::add);

        return bikeList.stream()
                .map(this::convertToDtoWithUser)
                .toList();
    }

}
