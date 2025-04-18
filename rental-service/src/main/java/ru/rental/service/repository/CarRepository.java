package ru.rental.service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.rental.service.entity.Car;

@Repository
public interface CarRepository extends CrudRepository<Car, Integer> {}
