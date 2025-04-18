package ru.rental.service.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import ru.rental.service.dto.BicycleDto;
import ru.rental.service.dto.BikeDto;
import ru.rental.service.dto.CarDto;
import ru.rental.service.dto.UserDto;
import ru.rental.service.entity.User;
import ru.rental.service.repository.BicycleRepository;
import ru.rental.service.repository.BikeRepository;
import ru.rental.service.repository.CarRepository;
import ru.rental.service.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final BikeRepository bikeRepository;

    private final CarRepository carRepository;

    private final BicycleRepository bicycleRepository;

    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public Optional<UserDto> findById(Integer id) {
        return userRepository.findById(id)
                .map(this::convertToDtoWithRelations);
    }

    @Transactional
    public UserDto create(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    @Transactional
    public Optional<UserDto> update(Integer id, UserDto userDto) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    updateUserFields(existingUser, userDto);
                    User updatedUser = userRepository.save(existingUser);
                    return convertToDtoWithRelations(updatedUser);
                });
    }

    @Transactional
    public boolean delete(Integer id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);

            return true;
        }

        return false;
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return ((List<User>) userRepository.findAll()).stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAllWithVehicles() {
        return ((List<User>) userRepository.findAll()).stream()
                .map(this::convertToDtoWithRelations)
                .toList();
    }

    private UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    private UserDto convertToDtoWithRelations(User user) {
        UserDto dto = convertToDto(user);
        dto.setBikes(getBikesForUser(user.getId()));
        dto.setCars(getCarsForUser(user.getId()));
        dto.setBicycles(getBicyclesForUser(user.getId()));
        return dto;
    }

    private void updateUserFields(User user, UserDto dto) {
        modelMapper.map(dto, user);
    }

    private List<BikeDto> getBikesForUser(Integer userId) {
        return bikeRepository.findById(userId).stream()
                .map(bike -> modelMapper.map(bike, BikeDto.class))
                .toList();
    }

    private List<CarDto> getCarsForUser(Integer userId) {
        return carRepository.findById(userId).stream()
                .map(car -> modelMapper.map(car, CarDto.class))
                .toList();
    }

    private List<BicycleDto> getBicyclesForUser(Integer userId) {
        return bicycleRepository.findById(userId).stream()
                .map(bicycle -> modelMapper.map(bicycle, BicycleDto.class))
                .toList();
    }
}
