package ru.rental.service.rental.controller;

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
import ru.rental.service.rental.dto.RentalDto;
import ru.rental.service.rental.dto.RentalDtoCreate;
import ru.rental.service.rental.service.RentalService;

import java.util.List;

@Tag(name = "Rental", description = "DAO REST controller rental")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rentals")
@Validated
public class RentalControllerRest {

    private final RentalService rentalService;

    @Operation(summary = "Get all rental",
            description = "Запрашиваем всю аренду у базы данных без какой либо выборки и батчей, датащим сразу все")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "при успешной поиске аренды вернем 200 код и аренду")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RentalDto> findByRentalId(@PathVariable Integer id) {
        return rentalService
                .findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<RentalDto>> findAll() {
        return ResponseEntity.ok(rentalService.getAll());
    }

    @PostMapping
    @Operation(summary = "Create rental")
    public ResponseEntity<RentalDto> create(@RequestBody @Valid RentalDtoCreate rentalDtoCreate) {
        RentalDto createdRental = rentalService.create(rentalDtoCreate);

        return ResponseEntity.status(201).body(createdRental);
    }

    @Operation(summary = "Delete a rental")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return rentalService.delete(id)
                ? ResponseEntity.status(HttpStatus.ACCEPTED).build()
                : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get rental by user ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RentalDto>> getRentalByUserId(
            @PathVariable Integer userId) {
        return ResponseEntity.ok(rentalService.findByUserId(userId));
    }

    @Operation(summary = "Update rental")
    @PutMapping("/{id}")
    public ResponseEntity<RentalDto> updateRental(
            @PathVariable Integer id,
            @RequestBody @Valid RentalDto rentalDto) {

        if (!id.equals(rentalDto.getId())) {
            throw new IllegalArgumentException("ID in path and body must match");
        }

        RentalDto updateRental = rentalService.update(rentalDto);
        return ResponseEntity.ok(updateRental);
    }
}
