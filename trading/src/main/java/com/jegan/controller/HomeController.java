package com.jegan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
//@RequestMapping("/api")
public class HomeController {

    @GetMapping("/hello")
    public String sayhello(){
        return "welcome to trading plateform";
    }
}
