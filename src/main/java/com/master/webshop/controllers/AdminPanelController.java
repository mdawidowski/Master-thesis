package com.master.webshop.controllers;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@Controller
public class AdminPanelController {
//    creating mapping

    @GetMapping("/admin/panel")
    public String getAdminPanel(Model theModel){
        return "adminPanel";
    }
}
