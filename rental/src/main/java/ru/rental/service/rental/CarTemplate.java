package ru.rental.service.rental;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.rental.service.common.dto.CarDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarTemplate {

    private final RestTemplate restTemplate;

    private String carServerUrl = "http://localhost:7876/api/cars";

    public CarDto findById(Integer id) {
        return restTemplate.getForObject(carServerUrl + "/" + id, CarDto.class, id);
    }

    public List<CarDto> findAllByUserId(Integer userId) {
        return restTemplate.getForObject(carServerUrl + "/user/" + userId, List.class, userId);
    }
}
