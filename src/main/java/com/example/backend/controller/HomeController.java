package com.example.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin(origins = "https://port-next-approved-front-m5mcnm8ebdc80276.sel4.cloudtype.app")
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";  // templates/index.html 반환
    }
}