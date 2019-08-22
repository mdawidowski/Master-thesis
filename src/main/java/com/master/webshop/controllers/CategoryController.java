package com.master.webshop.controllers;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@Controller
public class CategoryController {

    @GetMapping("/category")
    public String getCategories(Model theModel){
        return "categories";
    }
}