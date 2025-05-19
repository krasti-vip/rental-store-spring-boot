package ru.rental.service.config;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rental.service.dto.create.RentalDtoCreate;
import ru.rental.service.entity.Rental;
import ru.rental.service.repository.BicycleRepository;
import ru.rental.service.repository.BikeRepository;
import ru.rental.service.repository.CarRepository;
import ru.rental.service.repository.UserRepository;

@Component
public class RentalDtoToEntityConverter implements Converter<RentalDtoCreate, Rental> {

    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final BikeRepository bikeRepository;
    private final BicycleRepository bicycleRepository;

    @Autowired
    public RentalDtoToEntityConverter(UserRepository userRepository,
                                      CarRepository carRepository,
                                      BikeRepository bikeRepository,
                                      BicycleRepository bicycleRepository) {
        this.userRepository = userRepository;
        this.carRepository = carRepository;
        this.bikeRepository = bikeRepository;
        this.bicycleRepository = bicycleRepository;
    }

    @Override
    public Rental convert(MappingContext<RentalDtoCreate, Rental> context) {
        RentalDtoCreate dto = context.getSource();

        return Rental.builder()
                .user(userRepository.findById(dto.getUserId()).orElseThrow())
                .car(dto.getCarId() != null ? carRepository.findById(dto.getCarId()).orElse(null) : null)
                .bike(dto.getBikeId() != null ? bikeRepository.findById(dto.getBikeId()).orElse(null) : null)
                .bicycle(dto.getBicycleId() != null ? bicycleRepository.findById(dto.getBicycleId()).orElse(null) : null)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .rentalAmount(dto.getRentalAmount())
                .isPaid(Boolean.TRUE.equals(dto.getIsPaid()))
                .build();
    }
}
