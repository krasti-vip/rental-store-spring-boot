package ru.rental.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.rental.service.common.dto.BankCardDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankcardTemplate {

    private final RestTemplate restTemplate;

    @Value("${bankcard.url}")
    private String bankcardServerUrl;

    public BankCardDto findById(Integer id) {
        return restTemplate.getForObject(bankcardServerUrl + "/" + id, BankCardDto.class, id);
    }

    public List<BankCardDto> findAllByUserId(Integer userId) {
        ResponseEntity<List<BankCardDto>> response = restTemplate.exchange(
                bankcardServerUrl + "/user/" + userId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<BankCardDto>>() {}
        );

        return response.getBody();
    }
}
