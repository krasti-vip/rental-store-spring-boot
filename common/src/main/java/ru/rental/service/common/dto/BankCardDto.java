package ru.rental.service.common.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BankCardDto {

    private Integer id;

    private Integer userId;

    private String numberCard;

    private String expirationDate;

    private Integer secretCode;
}
