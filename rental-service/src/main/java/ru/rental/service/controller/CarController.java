package ru.rental.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.rental.service.dto.CarDto;
import ru.rental.service.service.CarService;

import java.util.List;

@Controller
@RequestMapping("/car")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public String getAllCars(Model model) {
        List<CarDto> cars = carService.getAll();
        model.addAttribute("cars", cars);
        return "car/index";
    }

    @GetMapping("/{id}")
    public String getCarById(@PathVariable("id") int id, Model model) {
        var car = carService.get(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "car no found id: " + id));
        model.addAttribute("car", car);
        return "car/car";
    }

    @GetMapping("/create")
    public String getCreateCarForm(Model model) {
        model.addAttribute("car", new CarDto());
        return "car/form";
    }

    @PostMapping
    public String createCar(@ModelAttribute("car") CarDto carDto) {
        carService.save(carDto);
        return "redirect:/car";
    }

    @DeleteMapping("/{id}")
    public String deleteCarById(@PathVariable("id") int id) {
        carService.delete(id);
        return "redirect:/car";
    }

    @PatchMapping("/{id}")
    public String updateCarById(@PathVariable("id") int id, CarDto carDto) {
        carService.update(id, carDto);
        return "redirect:/car";
    }

    @GetMapping("/{id}/edit")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        var car = carService.get(id).orElseThrow(() -> new IllegalArgumentException("car no found id: " + id));
        model.addAttribute("car", car);
        return "car/update";
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }
}
