package ru.rental.service.rental.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.rental.service.rental.entity.Rental;

import java.util.List;

@Repository
public interface RentalRepository extends CrudRepository<Rental, Integer> {

    List<Rental> findByUserId(Integer userId);
}
