package ru.rental.service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static record FieldErrorDto(String field, String message) {}

    private static record ValidationErrorResponse(int status, List<FieldErrorDto> fieldErrors) {

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleBodyValidation(MethodArgumentNotValidException exception) {
        final var errorDtoList = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new FieldErrorDto(err.getField(), err.getDefaultMessage()))
                .toList();

        final var errorResponse = new ValidationErrorResponse(HttpStatus.BAD_REQUEST.value(), errorDtoList);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
