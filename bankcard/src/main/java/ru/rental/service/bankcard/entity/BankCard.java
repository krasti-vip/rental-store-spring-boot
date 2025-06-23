package ru.rental.service.bankcard.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bank_cards")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BankCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "number_card", nullable = false)
    private String numberCard;

    @Column(name = "expiration_date", nullable = false)
    private String expirationDate;

    @Column(name = "secret_code", nullable = false)
    private Integer secretCode;

    @Column(name = "user_id", nullable = false)
    private Integer userId;


}
