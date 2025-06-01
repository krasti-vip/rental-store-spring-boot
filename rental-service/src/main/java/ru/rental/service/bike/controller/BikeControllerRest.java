package ru.rental.service.bike.controller;

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

import ru.rental.service.bike.service.BikeService;
import ru.rental.service.common.dto.BikeDto;
import ru.rental.service.common.dto.BikeDtoCreate;

import java.util.List;

@Tag(name = "Bike", description = "DAO REST controller bike")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bikes")
@Validated
public class BikeControllerRest {

    private final BikeService bikeService;

    @Operation(summary = "Get all bike",
            description = "Запрашиваем все мотоциклы у базы данных без какой либо выборки и батчей, датащим сразу все")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "при успешной поиске мотоциклов вернем 200 код и мотоциклы")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BikeDto> findById(@PathVariable Integer id) {
        return bikeService
                .findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<BikeDto>> findAll() {
        return ResponseEntity.ok(bikeService.getAll());
    }

    @PostMapping
    @Operation(summary = "Create bike")
    public ResponseEntity<BikeDto> create(@RequestBody @Valid BikeDtoCreate bikeDtoCreate) {
        BikeDto createdBike = bikeService.create(bikeDtoCreate);

        return ResponseEntity.status(201).body(createdBike);
    }

    @Operation(summary = "Delete a bike")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return bikeService.delete(id)
                ? ResponseEntity.status(HttpStatus.ACCEPTED).build()
                : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get bike by user ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BikeDto>> getByUserId(
            @PathVariable Integer userId) {
        return ResponseEntity.ok(bikeService.findByUserId(userId));
    }

    @Operation(summary = "Update bike")
    @PutMapping("/{id}")
    public ResponseEntity<BikeDto> update(
            @PathVariable Integer id,
            @RequestBody @Valid BikeDto bikeDto) {

        if (!id.equals(bikeDto.getId())) {
            throw new IllegalArgumentException("ID in path and body must match");
        }

        BikeDto updateBike = bikeService.update(bikeDto);
        return ResponseEntity.ok(updateBike);
    }
}
