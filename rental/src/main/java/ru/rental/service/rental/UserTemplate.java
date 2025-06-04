package ru.rental.service.rental;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.rental.service.common.dto.UserDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserTemplate {

    private final RestTemplate restTemplate;

    private final String userServerUrl = "http://localhost:8081/api/users";

    public UserDto findById(Integer id) {
        return restTemplate.getForObject(userServerUrl + "/" + id, UserDto.class, id);
    }

    public List<UserDto> findAllByUserId(Integer userId) {
        return restTemplate.getForObject(userServerUrl + "/user/" + userId, List.class, userId);
    }
}
