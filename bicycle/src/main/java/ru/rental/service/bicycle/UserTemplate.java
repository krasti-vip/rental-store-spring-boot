package ru.rental.service.bicycle;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.rental.service.common.dto.UserDto;

@Service
@RequiredArgsConstructor
public class UserTemplate {

    private final RestTemplate restTemplate;

    private String userServerUrl = "http://localhost:8081/api/users";

    public UserDto findById(Integer id) {
        return restTemplate.getForObject(userServerUrl + "/" + id, UserDto.class, id);
    }
}
