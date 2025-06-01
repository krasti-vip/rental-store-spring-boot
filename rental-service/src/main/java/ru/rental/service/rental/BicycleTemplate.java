package ru.rental.service.rental;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.rental.service.common.dto.BicycleDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BicycleTemplate {

    private final RestTemplate restTemplate;

    private String bicucleServerUrl = "http://localhost:7878/api/bicycles";

    public BicycleDto findById(Integer id) {
        return restTemplate.getForObject(bicucleServerUrl + "/" + id, BicycleDto.class, id);
    }

    public List<BicycleDto> findAllByUserId(Integer userId) {
        return restTemplate.getForObject(bicucleServerUrl + "/user/" + userId, List.class, userId);
    }
}
