package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = {"/", ""})
    public String list(Principal principal, Model model) {
        String currentUserName = principal.getName();
        User currentUser = userService.findByUserName(currentUserName);
        String msg = String.valueOf("mesage");
        model.addAttribute("msg", msg);
        model.addAttribute("user", currentUser);
        return "admin";
    }
}
