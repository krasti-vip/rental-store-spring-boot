package ru.rental.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.rental.service.common.dto.BikeDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BikeTemplate {

    private final RestTemplate restTemplate;

    @Value("${bike.url}")
    private String bikeServerUrl;

    public BikeDto findById(Integer id) {
        return restTemplate.getForObject(bikeServerUrl + "/" + id, BikeDto.class, id);
    }

    public List<BikeDto> findAllByUserId(Integer userId) {
        ResponseEntity<List<BikeDto>> response = restTemplate.exchange(
                bikeServerUrl + "/user/" + userId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<BikeDto>>() {
                }
        );

        return response.getBody();
    }
}
