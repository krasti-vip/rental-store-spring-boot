package ru.rental.service.user.controller;

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
import ru.rental.service.common.dto.UserDto;
import ru.rental.service.common.dto.UserDtoCreate;
import ru.rental.service.user.service.UserService;

import java.util.List;

@Tag(name = "User", description = "DAO REST controller user")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Validated
public class UserControllerRest {

    private final UserService userService;

    @Operation(summary = "Get all user",
            description = "Запрашиваем всех пользователей у базы данных без какой либо выборки и батчей, датащим сразу все")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "при успешной поиске пользователей вернем 200 код и пользователей")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Integer id) {
        return userService
                .findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @PostMapping
    @Operation(summary = "Create user")
    public ResponseEntity<UserDto> create(@RequestBody @Valid UserDtoCreate userDtoCreate) {
        UserDto createdUser = userService.create(userDtoCreate);

        return ResponseEntity.status(201).body(createdUser);
    }

    @Operation(summary = "Delete a user")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return userService.delete(id)
                ? ResponseEntity.status(HttpStatus.ACCEPTED).build()
                : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Update user")
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Integer id,
            @RequestBody @Valid UserDto userDto) {

        if (!id.equals(userDto.getId())) {
            throw new IllegalArgumentException("ID in path and body must match");
        }

        UserDto updateUser = userService.update(userDto);
        return ResponseEntity.ok(updateUser);
    }
}
