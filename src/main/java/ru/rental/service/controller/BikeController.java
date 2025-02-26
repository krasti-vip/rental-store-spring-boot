package ru.rental.service.controller;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.rental.service.dto.BikeDto;
import ru.rental.service.service.BikeService;

import java.util.List;

@Controller
@RequestMapping("/bike")
public class BikeController {

    private final BikeService bikeService;

    @Autowired
    public BikeController(BikeService bikeService) {
        this.bikeService = bikeService;
    }

    @GetMapping
    public String getAllBikes(Model model) {
        List<BikeDto> bikes = bikeService.getAll();
        model.addAttribute("bike", bikes);
        return "bike";
    }
}

