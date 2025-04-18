package ru.rental.service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.rental.service.entity.Bicycle;

@Repository
public interface BicycleRepository extends CrudRepository<Bicycle, Integer> {}
