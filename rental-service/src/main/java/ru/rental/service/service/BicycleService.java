package ru.rental.service.service;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rental.service.dao.BicycleDao;
import ru.rental.service.dto.BicycleDto;
import ru.rental.service.model.Bicycle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Component
public class BicycleService implements Service<BicycleDto, Integer> {

    private static final Logger log = LoggerFactory.getLogger(BicycleService.class);

    private static final String NO_BISYCLE_FOUND = "Bicycle with id {} not found";

    private final ModelMapper modelMapper;

    private final BicycleDao bicycleDao;

    @Autowired
    public BicycleService(BicycleDao bicycleDao, ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.bicycleDao = bicycleDao;
    }

    @Override
    public Optional<BicycleDto> get(Integer id) {
        final var maybeBicycle = bicycleDao.get(id);

        if (maybeBicycle == null) {
            log.warn(NO_BISYCLE_FOUND, id);
            return Optional.empty();
        } else {
            log.info("bicycle with id {} found", id);
            return Optional.of(modelMapper.map(maybeBicycle, BicycleDto.class));
        }
    }

    @Override
    public Optional<BicycleDto> update(Integer id, BicycleDto obj) {
        var maybeBicycle = bicycleDao.get(id);

        if (maybeBicycle == null) {
            log.warn(NO_BISYCLE_FOUND, id);
            return Optional.empty();
        }

        var updatedBicycle = Bicycle.builder()
                .id(maybeBicycle.getId())
                .model(obj.getModel())
                .price(obj.getPrice())
                .color(obj.getColor())
                .build();

        var updated = bicycleDao.update(id, updatedBicycle);
        log.info("bicycle with id {} updated", id);
        return Optional.of(modelMapper.map(updated, BicycleDto.class));
    }

    @Override
    public BicycleDto save(BicycleDto obj) {
        var newBicucle = Bicycle.builder()
                .model(obj.getModel())
                .price(obj.getPrice())
                .color(obj.getColor())
                .build();
        var savedBicycle = bicycleDao.save(newBicucle);
        log.info("bicycle with id {} saved", savedBicycle.getId());
        return modelMapper.map(savedBicycle, BicycleDto.class);
    }

    @Override
    public boolean delete(Integer id) {
        var maybeBicycle = bicycleDao.get(id);

        if (maybeBicycle == null) {
            log.warn(NO_BISYCLE_FOUND, id);
            return false;
        }
        log.info("bicycle with id {} deleted", id);
        return bicycleDao.delete(id);
    }

    @Override
    public List<BicycleDto> filterBy(Predicate<BicycleDto> predicate) {
        log.info("bicycle filtering by {}", predicate);
        return bicycleDao.getAll().stream()
                .map(e -> modelMapper.map(e, BicycleDto.class))
                .filter(predicate)
                .toList();
    }

    @Override
    public List<BicycleDto> getAll() {
        if (bicycleDao.getAll().isEmpty()) {
            log.info("No bicycle");
            return new ArrayList<>();
        }
        log.info("Bicecles found");
        return bicycleDao.getAll().stream().map(e -> modelMapper.map(e, BicycleDto.class)).toList();
    }

    public List<BicycleDto> getAllByUserId(int userId) {
        log.info("Fetching bicycle for user with id {}", userId);
        return bicycleDao.getAllByUserId(userId).stream()
                .map(e -> modelMapper.map(e, BicycleDto.class))
                .toList();
    }

    public Optional<BicycleDto> updateUserId(Integer bicycleId, Integer userId) {

        var updatedBicycle = bicycleDao.updateUserId(bicycleId, userId);

        if (updatedBicycle != null) {
            log.info("Bicycle with id {} successfully updated with userId {}", bicycleId, userId);
            return Optional.of(modelMapper.map(updatedBicycle, BicycleDto.class));
        }

        log.warn("Bicycle with id {} was not updated!", bicycleId);
        return Optional.empty();
    }
}
