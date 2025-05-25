package ru.rental.service.user.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import ru.rental.service.ServiceInterface;
import ru.rental.service.user.dto.UserDto;
import ru.rental.service.user.dto.UserDtoCreate;
import ru.rental.service.user.entity.User;
import ru.rental.service.user.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements ServiceInterface<UserDto, UserDtoCreate> {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Optional<UserDto> findById(Integer id) {
        return userRepository.findWithCardsById(id)
                .map(UserDto::toEntity);
    }

    @Transactional
    public UserDto create(UserDtoCreate userDtoCreate) {
        User user = userDtoCreate.toEntity();
        User savedUser = userRepository.save(user);
        return UserDto.toEntity(savedUser);
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

        return UserDto.toEntity(savedUser);
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
                .map(UserDto::toEntity)
                .toList();
    }
}
