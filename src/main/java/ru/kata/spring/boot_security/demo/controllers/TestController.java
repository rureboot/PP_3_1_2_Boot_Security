package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

    @RequestMapping("/test")
    public String test(Model model){
        model.addAttribute("msg1", "hallo m8");
        model.addAttribute("msg2", "second hallo");
        return "test";
    }
}
