package ru.rental.service;

import java.util.List;

public interface ServiceInterfaceUserId<D> {

    List<D> findByUserId(Integer userId);
}
