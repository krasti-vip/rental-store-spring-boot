package ru.rental.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankCard {

    private Integer id;

    private Integer userId;

    private String numberCard;

    private String expirationDate;

    private Integer secretCode;
}
