package ru.rental.service.car.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.rental.service.common.dto.CarDto;
import ru.rental.service.common.dto.CarDtoCreate;
import ru.rental.service.car.service.CarService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8081")
@Tag(name = "Car", description = "DAO REST controller car")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cars")
@Validated
public class CarControllerRest {

    private final CarService carService;

    @Operation(summary = "Get all car",
            description = "Запрашиваем все машины у базы данных без какой либо выборки и батчей, датащим сразу все")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "при успешной поиске машин вернем 200 код и машины")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CarDto> findById(@PathVariable Integer id) {
        return carService
                .findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<CarDto>> findAll() {
        return ResponseEntity.ok(carService.getAll());
    }

    @PostMapping
    @Operation(summary = "Create car")
    public ResponseEntity<CarDto> create(@RequestBody @Valid CarDtoCreate carDtoCreate) {
        CarDto createdCar = carService.create(carDtoCreate);

        return ResponseEntity.status(201).body(createdCar);
    }

    @Operation(summary = "Delete a car")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return carService.delete(id)
                ? ResponseEntity.status(HttpStatus.ACCEPTED).build()
                : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get car by user ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CarDto>> getCarByUserId(
            @PathVariable Integer userId) {
        return ResponseEntity.ok(carService.findByUserId(userId));
    }

    @Operation(summary = "Update car")
    @PutMapping("/{id}")
    public ResponseEntity<CarDto> updateCar(
            @PathVariable Integer id,
            @RequestBody @Valid CarDto carDto) {

        if (!id.equals(carDto.getId())) {
            throw new IllegalArgumentException("ID in path and body must match");
        }

        CarDto updateCar = carService.update(carDto);
        return ResponseEntity.ok(updateCar);
    }
}
