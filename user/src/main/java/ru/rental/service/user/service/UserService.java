package ru.rental.service.user.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rental.service.common.dto.*;
import ru.rental.service.common.service.ServiceInterface;
import ru.rental.service.user.*;
import ru.rental.service.user.entity.User;
import ru.rental.service.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements ServiceInterface<UserDto, UserDtoCreate> {

    private final UserRepository userRepository;

    private final MapperUtilUser mapperUtilUser;

    private final BicycleTemplate bicycleTemplate;

    private final CarTemplate carTemplate;

    private final BikeTemplate bikeTemplate;

    private final BankcardTemplate bankcardTemplate;

    @Transactional(readOnly = true)
    public Optional<UserDto> findById(Integer id) {
        return userRepository.findById(id)
                .map(user -> {
                    List<Integer> bicycleIds = bicycleTemplate.findAllByUserId(user.getId())
                            .stream()
                            .map(BicycleDto::getId)
                            .toList();

                    List<Integer> carIds = carTemplate.findAllByUserId(user.getId())
                            .stream()
                            .map(CarDto::getId)
                            .toList();

                    List<Integer> bikeIds = bikeTemplate.findAllByUserId(user.getId())
                            .stream()
                            .map(BikeDto::getId)
                            .toList();

                    List<Integer> bankCardIds = bankcardTemplate.findAllByUserId(user.getId())
                            .stream()
                            .map(BankCardDto::getId)
                            .toList();

                    user.setBicycleDtoId(bicycleIds);
                    user.setCarDtoId(carIds);
                    user.setBikeDtoId(bikeIds);
                    user.setBankCardDtoId(bankCardIds);

                    return mapperUtilUser.toDto(user);
                });
    }

    @Transactional
    public UserDto create(UserDtoCreate userDtoCreate) {
        User user = mapperUtilUser.toEntity(userDtoCreate);
        User savedUser = userRepository.save(user);
        return mapperUtilUser.toDto(savedUser);
    }

    @Transactional
    public UserDto update(UserDto updateDto) {
        User existingUser = userRepository.findById(updateDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        existingUser.setUserName(updateDto.getUserName());
        existingUser.setFirstName(updateDto.getFirstName());
        existingUser.setLastName(updateDto.getLastName());
        existingUser.setPassport(updateDto.getPassport());
        existingUser.setEmail(updateDto.getEmail());

        User savedUser = userRepository.save(existingUser);

        return mapperUtilUser.toDto(savedUser);
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
    public List<UserDto> getAll() {
        return ((List<User>) userRepository.findAll()).stream()
                .map(mapperUtilUser::toDto)
                .toList();
    }
}
