package ru.rental.service.car.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "cars")
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "horse_power", nullable = false)
    private Integer horsePower;

    @Column(name = "volume", nullable = false)
    private Double volume;

    @Column(name = "color")
    private String color;

    @Column(name = "user_id", nullable = false)
    private Integer userId;
}
