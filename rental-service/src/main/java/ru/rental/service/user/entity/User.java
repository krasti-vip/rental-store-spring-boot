package ru.rental.service.user.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.rental.service.bankcard.entity.BankCard;
import ru.rental.service.bicycle.entity.Bicycle;
import ru.rental.service.bike.entity.Bike;
import ru.rental.service.car.entity.Car;

import java.util.List;

@Table(name = "users")
@ToString(exclude = {"bankCards", "bikes", "cars", "bicycles"})
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "passport", nullable = false)
    private Long passport;

    @Column(name = "email")
    private String email;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private List<BankCard> bankCards;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private List<Bike> bikes;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private List<Car> cars;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private List<Bicycle> bicycles;
}
