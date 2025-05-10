package ru.rental.service.service;

import jakarta.persistence.EntityNotFoundException;
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
import ru.rental.service.dto.create.UserDtoCreate;
import ru.rental.service.entity.User;
import ru.rental.service.repository.BicycleRepository;
import ru.rental.service.repository.BikeRepository;
import ru.rental.service.repository.CarRepository;
import ru.rental.service.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements ServiceInterface<UserDto, UserDtoCreate> {

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
    public UserDto create(UserDtoCreate userDtoCreate) {
        User user = modelMapper.map(userDtoCreate, User.class);
        User savedUser = userRepository.save(user);

        return convertToDto(savedUser);
    }

    @Transactional
    public UserDto update(UserDto updateUserDto) {
        User existingUser = userRepository.findById(updateUserDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        modelMapper.map(updateUserDto, existingUser);
        User savedUser = userRepository.save(existingUser);

        return convertToDto(savedUser);

    }

    @Transactional
    public boolean delete(Integer id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);

            return true;
        }

        return false;
    }

    @Override
    public List<UserDto> findByUserId(Integer userId) {
        return List.of();
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAll() {
        return ((List<User>) userRepository.findAll()).stream()
                .map(this::convertToDto)
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
