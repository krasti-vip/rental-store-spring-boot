package ru.rental.service.bike.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.rental.service.bike.entity.Bike;

import java.util.List;

@Repository
public interface BikeRepository extends CrudRepository<Bike, Integer> {

    List<Bike> findByUserId(Integer userId);
}
