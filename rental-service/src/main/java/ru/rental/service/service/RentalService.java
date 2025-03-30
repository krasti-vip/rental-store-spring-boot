package ru.rental.service.service;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rental.service.dao.RentalDao;
import ru.rental.service.dto.RentalDto;
import ru.rental.service.model.Rental;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Component
public class RentalService implements Service<RentalDto, Integer> {

    private static final Logger log = LoggerFactory.getLogger(RentalService.class);

    private final ModelMapper modelMapper;

    private final RentalDao rentalDao;

    @Autowired
    public RentalService(RentalDao rentalDao, ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.rentalDao = rentalDao;
    }

    @Override
    public Optional<RentalDto> get(Integer id) {
        final var maybeRental = rentalDao.get(id);

        if (maybeRental == null) {
            log.warn("Rental not found");
            return Optional.empty();
        } else {
            log.info("Rental found");
            return Optional.of(modelMapper.map(maybeRental, RentalDto.class));
        }
    }

    @Override
    public Optional<RentalDto> update(Integer id, RentalDto obj) {
        var maybeRental = rentalDao.get(id);

        if (maybeRental == null) {
            log.warn("Rental not found");
            return Optional.empty();
        }

        var updatedRental = Rental.builder()
                .userId(obj.getUserId())
                .carId(obj.getCarId())
                .bikeId(obj.getBikeId())
                .startDate(obj.getStartDate())
                .endDate(obj.getEndDate())
                .rentalAmount(obj.getRentalAmount())
                .isPaid(obj.getIsPaid())
                .build();

        var updated = rentalDao.update(id, updatedRental);
        log.info("Rental updated");
        return Optional.of(modelMapper.map(updated, RentalDto.class));
    }

    @Override
    public RentalDto save(RentalDto obj) {
        var newRental = Rental.builder()
                .userId(obj.getUserId())
                .carId(obj.getCarId())
                .bikeId(obj.getBikeId())
                .startDate(obj.getStartDate())
                .endDate(obj.getEndDate())
                .rentalAmount(obj.getRentalAmount())
                .isPaid(obj.getIsPaid())
                .build();

        var savedRental = rentalDao.save(newRental);
        log.info("Rental saved");
        return modelMapper.map(savedRental, RentalDto.class);
    }

    @Override
    public boolean delete(Integer id) {
        var maybeRental = rentalDao.get(id);

        if (maybeRental == null) {
            log.info("Rental not found");
            return false;
        }
        log.info("Rental deleted");
        return rentalDao.delete(id);
    }

    @Override
    public List<RentalDto> filterBy(Predicate<RentalDto> predicate) {
        List<Rental> rentals = rentalDao.getAll();
        log.info("Rentals found");
        return rentals.stream()
                .map(e -> modelMapper.map(e, RentalDto.class))
                .filter(predicate)
                .toList();
    }

    @Override
    public List<RentalDto> getAll() {
        List<Rental> rentals = rentalDao.getAll();
        log.info("Rentals found");
        return rentals.stream()
                .map(e -> modelMapper.map(e, RentalDto.class))
                .toList();
    }
}
