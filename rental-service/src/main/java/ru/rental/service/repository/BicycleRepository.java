package ru.rental.service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.rental.service.entity.Bicycle;

import java.util.List;

@Repository
public interface BicycleRepository extends CrudRepository<Bicycle, Integer> {

    List<Bicycle> findByUserId(Integer userId);
}
