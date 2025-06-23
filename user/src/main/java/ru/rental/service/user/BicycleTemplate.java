package ru.rental.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.rental.service.common.dto.BicycleDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BicycleTemplate {

    private final RestTemplate restTemplate;

    @Value("${bicycle.url}")
    private String bicycleServerUrl;

    public BicycleDto findById(Integer id) {
        return restTemplate.getForObject(bicycleServerUrl + "/" + id, BicycleDto.class, id);
    }

    public List<BicycleDto> findAllByUserId(Integer userId) {
        ResponseEntity<List<BicycleDto>> response = restTemplate.exchange(
                bicycleServerUrl + "/user/" + userId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<BicycleDto>>() {}
        );

        return response.getBody();
    }
}
