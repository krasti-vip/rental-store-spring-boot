package ru.rental.service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.rental.service.entity.Rental;

import java.util.List;

@Repository
public interface RentalRepository extends CrudRepository<Rental, Integer> {

    List<Rental> findAllByUserId(Integer userId);

    List<Rental> findByUserId(Integer userId);
}
