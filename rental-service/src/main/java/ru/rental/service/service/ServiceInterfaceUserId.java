package ru.rental.service.service;

import java.util.List;

public interface ServiceInterfaceUserId<D> {

    List<D> findByUserId(Integer userId);
}
