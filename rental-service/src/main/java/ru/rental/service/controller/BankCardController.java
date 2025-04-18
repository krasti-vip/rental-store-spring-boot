package ru.rental.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.rental.service.dto.BankCardDto;
import ru.rental.service.service.BankCardService;

import java.util.List;

@Controller
@RequestMapping("/bankcard")
public class BankCardController {

    private static final String RETURN_A_BANK_CARD = "redirect:/bankcard";

    private final BankCardService bankCardService;

    @Autowired
    public BankCardController(BankCardService bankCardService) {
        this.bankCardService = bankCardService;
    }

    @GetMapping
    public String getAllBC(Model model) {
        List<BankCardDto> bankcard = bankCardService.getAll();
        model.addAttribute("bankcards", bankcard);
        return "bankcard/index";
    }

    @GetMapping("/{id}")
    public String getBCById(@PathVariable("id") int id, Model model) {
        var bankcard = bankCardService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "bankCard no found id: " + id));
        model.addAttribute("bankcard", bankcard);
        return "bankcard/bankcard";
    }

    @GetMapping("/create")
    public String getCreateBCForm(Model model) {
        model.addAttribute("bankcard", new BankCardDto());
        return "bankcard/form";
    }

    @PostMapping
    public String createBC(@ModelAttribute("bankcard") BankCardDto bankCardDto) {
        bankCardService.create(bankCardDto);
        return RETURN_A_BANK_CARD;
    }

    @DeleteMapping("/{id}")
    public String deleteBCById(@PathVariable("id") int id) {
        bankCardService.delete(id);
        return RETURN_A_BANK_CARD;
    }

    @PatchMapping("/{id}")
    public String updateBCById(@PathVariable("id") int id, BankCardDto bankCardDto) {
        bankCardService.update(id, bankCardDto);
        return RETURN_A_BANK_CARD;
    }

    @GetMapping("/{id}/edit")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        var bankcard = bankCardService.findById(id).orElseThrow(() -> new IllegalArgumentException("bankcard no found id: " + id));
        model.addAttribute("bankcard", bankcard);
        return "bankcard/update";
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }
}
