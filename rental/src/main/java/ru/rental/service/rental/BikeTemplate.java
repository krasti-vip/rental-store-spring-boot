package ru.rental.service.rental;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.rental.service.common.dto.BikeDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BikeTemplate {

    private final RestTemplate restTemplate;

    private String bikeServerUrl = "http://localhost:7877/api/bikes";

    public BikeDto findById(Integer id) {
        return restTemplate.getForObject(bikeServerUrl + "/" + id, BikeDto.class, id);
    }

    public List<BikeDto> findAllByUserId(Integer userId) {
        return restTemplate.getForObject(bikeServerUrl + "/user/" + userId, List.class, userId);
    }
}
