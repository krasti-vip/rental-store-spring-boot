package ru.rental.service.bankcard;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.rental.service.common.dto.UserDto;

@Service
@RequiredArgsConstructor
public class UserTemplate {

    private final RestTemplate restTemplate;

    @Value("${user.url}")
    private String userServerUrl;

    public UserDto findById(Integer id) {
        return restTemplate.getForObject(userServerUrl + "/" + id, UserDto.class, id);
    }
}
