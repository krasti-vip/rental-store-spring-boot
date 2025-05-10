package ru.rental.service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.rental.service.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {}
