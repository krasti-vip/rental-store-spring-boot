package ru.rental.service.controller;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
        model.addAttribute("bikes", bikes);
        return "bike/index";
    }

    @GetMapping("/{id}")
    public String getBikeById(@PathVariable("id") int id, Model model) {
        var bike = bikeService.get(id).orElseThrow(() -> new IllegalArgumentException("bike no found id: " + id));
        model.addAttribute("bike", bike);
        return "bike/bike";
    }

    @GetMapping("/create")
    public String getCreateBikeForm(Model model) {
        model.addAttribute("bike", new BikeDto());
        return "bike/form";
    }

    @PostMapping
    public String createBike(@ModelAttribute("bike") BikeDto bikeDto) {
        bikeService.save(bikeDto);
        return "redirect:/bike";
    }

    @DeleteMapping("/{id}")
    public String deleteBikeById(@PathVariable("id") int id) {
        bikeService.delete(id);
        return "redirect:/bike";
    }

    @PatchMapping("/{id}")
    public String updateBikeById(@PathVariable("id") int id, BikeDto bikeDto) {
        bikeService.update(id, bikeDto);
        return "redirect:/bike";
    }

    @GetMapping("/{id}/edit")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        var bike = bikeService.get(id).orElseThrow(() -> new IllegalArgumentException("bike no found id: " + id));
        model.addAttribute("bike", bike);
        return "bike/update";
    }
}

