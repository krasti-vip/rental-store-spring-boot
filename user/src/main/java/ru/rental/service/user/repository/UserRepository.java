package ru.rental.service.user.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.rental.service.user.entity.User;


@Repository
public interface UserRepository extends CrudRepository<User, Integer> {}
