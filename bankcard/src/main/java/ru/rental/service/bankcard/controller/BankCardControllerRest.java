package ru.rental.service.bankcard.controller;

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
import ru.rental.service.common.dto.BankCardDto;
import ru.rental.service.common.dto.BankCardDtoCreate;
import ru.rental.service.bankcard.service.BankCardService;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8081")
@Tag(name = "Bank Card", description = "DAO REST controller Bank Card")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bankcards")
@Validated
public class BankCardControllerRest {

    private final BankCardService bankCardService;

    @Operation(summary = "Get all bank card",
            description = "Запрашиваем все банковские карты у базы данных без какой либо выборки и батчей, датащим сразу все")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "при успешной поиске карты вернем 200 код и карту")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BankCardDto> findById(@PathVariable Integer id) {
        return bankCardService
                .findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<BankCardDto>> findAll() {
        return ResponseEntity.ok(bankCardService.getAll());
    }

    @PostMapping
    @Operation(summary = "Create bank card")
    public ResponseEntity<BankCardDto> create(@RequestBody @Valid BankCardDtoCreate bankCardDtoCreate) {
        BankCardDto createdCard = bankCardService.create(bankCardDtoCreate);

        return ResponseEntity.status(201).body(createdCard);
    }

    @Operation(summary = "Delete a bank card")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return bankCardService.delete(id)
                ? ResponseEntity.status(HttpStatus.ACCEPTED).build()
                : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get cards by user ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BankCardDto>> getByUserId(
            @PathVariable Integer userId) {
        return ResponseEntity.ok(bankCardService.findByUserId(userId));
    }

    @Operation(summary = "Update bank card")
    @PutMapping("/{id}")
    public ResponseEntity<BankCardDto> update(
            @PathVariable Integer id,
            @RequestBody @Valid BankCardDto bankCardDto) {

        if (!id.equals(bankCardDto.getId())) {
            throw new IllegalArgumentException("ID in path and body must match");
        }

        BankCardDto updatedCard = bankCardService.update(bankCardDto);
        return ResponseEntity.ok(updatedCard);
    }
}
