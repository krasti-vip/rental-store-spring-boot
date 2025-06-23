package ru.rental.service.user.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Table(name = "users")
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

    @Transient
    private List<Integer> bankCardDtoId;

    @Transient
    private List<Integer> bikeDtoId;

    @Transient
    private List<Integer> carDtoId;

    @Transient
    private List<Integer> bicycleDtoId;
}
