package ru.rental.service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.rental.service.entity.BankCard;

import java.util.List;

@Repository
public interface BankCardRepository extends CrudRepository<BankCard, Integer> {

    List<BankCard> findByUserId(Integer userId);
}
