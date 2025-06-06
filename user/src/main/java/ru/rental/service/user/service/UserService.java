package ru.rental.service.user.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import ru.rental.service.common.dto.BicycleDto;
import ru.rental.service.common.dto.UserDto;
import ru.rental.service.common.dto.UserDtoCreate;
import ru.rental.service.common.service.ServiceInterface;
import ru.rental.service.user.BicycleTemplate;
import ru.rental.service.user.MapperUtilUser;
import ru.rental.service.user.entity.User;
import ru.rental.service.user.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements ServiceInterface<UserDto, UserDtoCreate> {

    private final UserRepository userRepository;

    private final MapperUtilUser mapperUtilUser;

    private final BicycleTemplate bicycleTemplate;

    @Transactional(readOnly = true)
    public Optional<UserDto> findById(Integer id) {
        return userRepository.findById(id).map(mapperUtilUser::toDto);
//        return userRepository.findWithUserById(id)
//                .map(e -> {
//                            List<BicycleDto> bicycleDto = bicycleTemplate.findAllByUserId(e.getId());
//                            e.setBicyclesId(bicycleDto.stream().map(BicycleDto::getId).toList());
//                            return mapperUtilUser.toDto(e);
//                        }
//                );
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
