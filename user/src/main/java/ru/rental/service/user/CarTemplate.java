package ru.rental.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.rental.service.common.dto.CarDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarTemplate {

    private final RestTemplate restTemplate;

    @Value("${car.url}")
    private String carServerUrl;

    public CarDto findById(Integer id) {
        return restTemplate.getForObject(carServerUrl + "/" + id, CarDto.class, id);
    }

    public List<CarDto> findAllByUserId(Integer userId) {
        ResponseEntity<List<CarDto>> response = restTemplate.exchange(
                carServerUrl + "/user/" + userId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CarDto>>() {}
        );

        return response.getBody();
    }
}