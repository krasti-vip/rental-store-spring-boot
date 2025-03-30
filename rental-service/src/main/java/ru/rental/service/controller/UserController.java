package ru.rental.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.rental.service.dto.UserDto;
import ru.rental.service.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final String RETURN_A_USER = "redirect:/user";
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getAllUsers(Model model) {
        List<UserDto> users = userService.getAll();
        model.addAttribute("users", users);
        return "user/index";
    }

    @GetMapping("/{id}")
    public String getUserById(@PathVariable("id") int id, Model model) {
        var user = userService.get(id).orElseThrow(() -> new IllegalArgumentException("user no found id: " + id));
        model.addAttribute("user", user);
        return "user/user";
    }

    @GetMapping("/create")
    public String getCreateUserForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "user/form";
    }

    @PostMapping
    public String createUser(@ModelAttribute("user") UserDto userDto) {
        userService.save(userDto);
        return RETURN_A_USER;
    }

    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable("id") int id) {
        userService.delete(id);
        return RETURN_A_USER;
    }

    @PatchMapping("/{id}")
    public String updateUserById(@PathVariable("id") int id, UserDto userDto) {
        userService.update(id, userDto);
        return RETURN_A_USER;
    }

    @GetMapping("/{id}/edit")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        var user = userService.get(id).orElseThrow(() -> new IllegalArgumentException("user no found id: " + id));
        model.addAttribute("user", user);
        return "user/update";
    }
}
