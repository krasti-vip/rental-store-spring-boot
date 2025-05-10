package ru.rental.service.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.rental.service.dto.UserDto;
import ru.rental.service.entity.User;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setSkipNullEnabled(true);

        modelMapper.createTypeMap(UserDto.class, User.class)
                .addMappings(mapper -> {
                    mapper.skip(User::setId);
                    mapper.skip(User::setBicycles);
                    mapper.skip(User::setBikes);
                    mapper.skip(User::setCars);
                    mapper.map(UserDto::getUserName, User::setUserName);
                    mapper.map(UserDto::getFirstName, User::setFirstName);
                    mapper.map(UserDto::getLastName, User::setLastName);
                    mapper.map(UserDto::getPassport, User::setPassport);
                    mapper.map(UserDto::getEmail, User::setEmail);
                });

        return modelMapper;
    }
}
