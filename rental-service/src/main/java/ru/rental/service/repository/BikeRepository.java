package ru.rental.service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.rental.service.entity.Bike;

import java.util.List;

@Repository
public interface BikeRepository extends CrudRepository<Bike, Integer> {

    List<Bike> findByUserId(Integer userId);
}
