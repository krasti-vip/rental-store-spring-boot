package ru.rental.service.controller;

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
import ru.rental.service.dto.BicycleDto;
import ru.rental.service.dto.create.BicycleDtoCreate;
import ru.rental.service.service.BicycleService;

import java.util.List;

@Tag(name = "Bicycle", description = "DAO REST controller Bicycle")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bicycles")
@Validated
public class BicycleControllerRest {

    private final BicycleService bicycleService;

    @Operation(summary = "Get all bicycle",
            description = "Запрашиваем все велосипеды у базы данных без какой либо выборки и батчей, датащим сразу все")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "при успешной поиске велика вернем 200 код и велосипед")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BicycleDto> findById(@PathVariable Integer id) {
        return bicycleService
                .findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<BicycleDto>> findAll() {
        return ResponseEntity.ok(bicycleService.getAll());
    }

    @PostMapping
    @Operation(summary = "Create bicycle")
    public ResponseEntity<BicycleDto> create(@RequestBody @Valid BicycleDtoCreate bicycleDtoCreate) {
        BicycleDto createdBicycle = bicycleService.create(bicycleDtoCreate);

        return ResponseEntity.status(201).body(createdBicycle);
    }

    @Operation(summary = "Delete a bicycle")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return bicycleService.delete(id)
                ? ResponseEntity.status(HttpStatus.ACCEPTED).build()
                : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get bicycles by user ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BicycleDto>> getByUserId(
            @PathVariable Integer userId) {
        return ResponseEntity.ok(bicycleService.findByUserId(userId));
    }

    @Operation(summary = "Update bicycle")
    @PutMapping("/{id}")
    public ResponseEntity<BicycleDto> update(
            @PathVariable Integer id,
            @RequestBody @Valid BicycleDto bicycleDto) {

        if (!id.equals(bicycleDto.getId())) {
            throw new IllegalArgumentException("ID in path and body must match");
        }

        BicycleDto updatedBicycle = bicycleService.update(bicycleDto);
        return ResponseEntity.ok(updatedBicycle);
    }
}
