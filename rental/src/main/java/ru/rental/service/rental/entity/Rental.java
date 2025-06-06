package ru.rental.service.rental.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rentals")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "car_id", nullable = false)
    private Integer carId;

    @Column(name = "bike_id", nullable = false)
    private Integer bikeId;

    @Column(name = "bicycle_id", nullable = false)
    private Integer bicycleId;

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
