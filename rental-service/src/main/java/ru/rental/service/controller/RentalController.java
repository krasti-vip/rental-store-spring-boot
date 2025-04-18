package ru.rental.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.rental.service.dto.RentalDto;
import ru.rental.service.service.RentalService;

import java.util.List;

@Controller
@RequestMapping("/rental")
public class RentalController {

    private static final String RETURN_A_RENTAL = "redirect:/rental";

    private final RentalService rentalService;

    @Autowired
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    public String getAllRental(Model model) {
        List<RentalDto> rentals = rentalService.findAll();
        model.addAttribute("rentals", rentals);
        return "rental/index";
    }

    @GetMapping("/{id}")
    public String getRentalById(@PathVariable("id") int id, Model model) {
        var rental = rentalService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "rental no found id: " + id));
        model.addAttribute("rental", rental);
        return "rental/rental";
    }

    @GetMapping("/create")
    public String getCreateRentalForm(Model model) {
        model.addAttribute("rental", new RentalDto());
        return "rental/form";
    }

    @PostMapping
    public String createRental(@ModelAttribute("rental") RentalDto rentalDto) {
        rentalService.create(rentalDto);
        return RETURN_A_RENTAL;
    }

    @DeleteMapping("/{id}")
    public String deleteRentalById(@PathVariable("id") int id) {
        rentalService.delete(id);
        return RETURN_A_RENTAL;
    }

    @PatchMapping("/{id}")
    public String updateUserById(@PathVariable("id") int id, @ModelAttribute("rental") RentalDto rentalDto) {
        rentalService.update(id, rentalDto);
        return RETURN_A_RENTAL;
    }

    @GetMapping("/{id}/edit")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        var rental = rentalService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "car no found id: " + id));
        model.addAttribute("rental", rental);
        return "rental/update";
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }
}
