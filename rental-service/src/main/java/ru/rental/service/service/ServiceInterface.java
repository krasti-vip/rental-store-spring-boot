package ru.rental.service.service;

import java.util.List;
import java.util.Optional;

public interface ServiceInterface<D, T> {

    Optional<D> findById(Integer id);

    D create(T t);

    D update(D d);

    boolean delete(Integer id);

    List<D> findByUserId(Integer userId);

    List<D> getAll();
}
