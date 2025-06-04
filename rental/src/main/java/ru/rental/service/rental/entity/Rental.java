package ru.rental.service.rental.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rentals")
@ToString(exclude = {"user", "car", "bike", "bicycle"})
@EqualsAndHashCode(exclude = {"user", "car", "bike", "bicycle"})
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private Integer carId;

    private Integer bikeId;

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
