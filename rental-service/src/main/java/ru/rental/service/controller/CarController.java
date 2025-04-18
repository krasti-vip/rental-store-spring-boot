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

    private static final String RETURN_A_CAR = "redirect:/car";

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public String getAllCars(Model model) {
        List<CarDto> cars = carService.findAll();
        model.addAttribute("cars", cars);
        return "car/index";
    }

    @GetMapping("/{id}")
    public String getCarById(@PathVariable("id") int id, Model model) {
        var car = carService.findById(id)
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
        carService.create(carDto);
        return RETURN_A_CAR;
    }

    @DeleteMapping("/{id}")
    public String deleteCarById(@PathVariable("id") int id) {
        carService.delete(id);
        return RETURN_A_CAR;
    }

    @PatchMapping("/{id}")
    public String updateCarById(@PathVariable("id") int id, CarDto carDto) {
        carService.update(id, carDto);
        return RETURN_A_CAR;
    }

    @GetMapping("/{id}/edit")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        var car = carService.findById(id).orElseThrow(() -> new IllegalArgumentException("car no found id: " + id));
        model.addAttribute("car", car);
        return "car/update";
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }
}
