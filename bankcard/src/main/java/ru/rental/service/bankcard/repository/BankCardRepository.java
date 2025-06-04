package ru.rental.service.bankcard.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.rental.service.bankcard.entity.BankCard;

import java.util.List;

@Repository
public interface BankCardRepository extends CrudRepository<BankCard, Integer> {

    List<BankCard> findByUserId(Integer userId);
}
