package ru.rental.service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "rentals")
@ToString(exclude = {"user", "car", "bike"})
@EqualsAndHashCode(exclude = {"user", "car", "bike"})
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bike_id")
    private Bike bike;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bicycle_id")
    private Bicycle bicycle;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "rental_amount", nullable = false)
    private Double rentalAmount;

    @Column(name = "is_paid", nullable = false)
    private Boolean isPaid = false;

    public boolean isActive() {
        return endDate == null || endDate.isAfter(LocalDateTime.now());
    }
}
