package ru.rental.service.bicycle.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.rental.service.bicycle.entity.Bicycle;

import java.util.List;

@Repository
public interface BicycleRepository extends CrudRepository<Bicycle, Integer> {

    List<Bicycle> findByUserId(Integer userId);
}
