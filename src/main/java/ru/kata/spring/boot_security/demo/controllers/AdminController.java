package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = {"/", ""})
    public String list(Principal principal, Model model) {
        String currentUserName = principal.getName();
        User currentUser = userService.findByUserName(currentUserName);
        List<User> users = userService.findAll();

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("users", users);


        return "admin";
    }

    @GetMapping("/userForm")
    public String userForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);

        List<Role> roles = userService.findAllRoles();
        model.addAttribute("roles", roles);
        return "userForm";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user) {
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam Long id, Model model) {

        User user = userService.findById(id);
        user.setPassword("leave old password");
        List<Role> roles = userService.findAllRoles();

        model.addAttribute("user",user);
        model.addAttribute("roles", roles);

        return "userForm";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }

}
