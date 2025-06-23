package ru.rental.service.bike.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bikes")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Bike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "horse_power", nullable = false)
    private Integer horsePower;

    @Column(name = "volume", nullable = false)
    private Double volume;

    @Column(name = "user_id", nullable = false)
    private Integer userId;
}
