package com.master.webshop.controllers;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@Controller
public class InvoiceController {

    @GetMapping("/invoice")
    public String getInvoices(Model theModel){
        return "invoices";
    }
}