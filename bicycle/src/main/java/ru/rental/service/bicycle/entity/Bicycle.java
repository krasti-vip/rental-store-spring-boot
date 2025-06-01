package ru.rental.service.bicycle.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bicycles")
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Bicycle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "color")
    private String color;

    private Integer userId;
}
