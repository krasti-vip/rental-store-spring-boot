package ru.rental.service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.rental.service.entity.Bike;

@Repository
public interface BikeRepository extends CrudRepository<Bike, Integer> {}
