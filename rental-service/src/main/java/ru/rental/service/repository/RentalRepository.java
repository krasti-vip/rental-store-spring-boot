package ru.rental.service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.rental.service.entity.Rental;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RentalRepository extends CrudRepository<Rental, Integer> {
    List<Rental> findByEndDateIsNullOrEndDateAfter(LocalDateTime date);
}
