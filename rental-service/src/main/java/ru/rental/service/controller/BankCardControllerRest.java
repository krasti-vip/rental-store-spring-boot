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
import ru.rental.service.dto.BankCardDto;
import ru.rental.service.dto.BankCardDtoCreate;
import ru.rental.service.service.BankCardService;

import java.util.List;

@Tag(name = "Bank Card", description = "DAO REST controller Bank Card")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bankcards")
@Validated
public class BankCardControllerRest {

    private final BankCardService bankCardService;

    @Operation(summary = "Get all bank card",
            description = "Запррашиваем все банковские карты у базы данных без какой либо выборки и батчей, датащим сразу все")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "при успешной поиске карты вернем 200 код и карту")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BankCardDto> findByCardId(@PathVariable Integer id) {
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
        final var bankCardDto = bankCardService.create(BankCardDto
                .builder()
                .numberCard(bankCardDtoCreate.getNumberCard())
                .expirationDate(bankCardDtoCreate.getExpirationDate())
                .userId(bankCardDtoCreate.getUserId())
                .secretCode(bankCardDtoCreate.getSecretCode())
                .build()
        );

        return ResponseEntity.status(201).body(bankCardDto);
    }

    @Operation(summary = "Delete a bank card")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return bankCardService.delete(id)
                ? ResponseEntity.status(HttpStatus.ACCEPTED).build()
                : ResponseEntity.notFound().build();
    }
}
