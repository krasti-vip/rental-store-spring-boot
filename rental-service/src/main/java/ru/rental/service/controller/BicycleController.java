package ru.rental.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.rental.service.dto.BicycleDto;
import ru.rental.service.service.BicycleService;
import java.util.List;

@Controller
@RequestMapping("/bicycle")
public class BicycleController {

    private static final String RETURN_A_BICYCLE = "redirect:/bicycle";

    private final BicycleService bicycleService;

    @Autowired
    public BicycleController(BicycleService bicycleService) {
        this.bicycleService = bicycleService;
    }

    @GetMapping
    public String getAllBicycles(Model model) {
        List<BicycleDto> bicycle = bicycleService.findAll();
        model.addAttribute("bicycles", bicycle);
        return "bicycle/index";
    }

    @GetMapping("/{id}")
    public String getBicycleById(@PathVariable("id") int id, Model model) {
        var bicycle = bicycleService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "bicycle no found id: " + id));
        model.addAttribute("bicycle", bicycle);
        return "bicycle/bicycle";
    }

    @GetMapping("/create")
    public String getCreateBicycleForm(Model model) {
        model.addAttribute("bicycle", new BicycleDto());
        return "bicycle/form";
    }

    @PostMapping
    public String createBicycle(@ModelAttribute("bicycle") BicycleDto bicycleDto) {
        bicycleService.create(bicycleDto);
        return RETURN_A_BICYCLE;
    }

    @DeleteMapping("/{id}")
    public String deleteBicycleById(@PathVariable("id") int id) {
        bicycleService.delete(id);
        return RETURN_A_BICYCLE;
    }

    @PatchMapping("/{id}")
    public String updateBicycleById(@PathVariable("id") int id, BicycleDto bicycleDto) {
        bicycleService.update(id, bicycleDto);
        return RETURN_A_BICYCLE;
    }

    @GetMapping("/{id}/edit")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        var bicycle = bicycleService.findById(id).orElseThrow(() -> new IllegalArgumentException("bicycle no found id: " + id));
        model.addAttribute("bicycle", bicycle);
        return "bicycle/update";
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }
}
