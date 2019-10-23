package com.master.webshop.controllers;

import com.master.webshop.model.Apriori;
import com.master.webshop.model.Product;
import com.master.webshop.services.CartItemService;
import com.master.webshop.services.OrderService;
import com.master.webshop.services.ProductService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Component
@Controller
public class AdminPanelController {
//    creating mapping

    ProductService productService;

    CartItemService cartItemService;

    OrderService orderService;

    public AdminPanelController(ProductService productService, CartItemService cartItemService, OrderService orderService) {
        this.productService = productService;
        this.cartItemService = cartItemService;
        this.orderService = orderService;
    }

    @GetMapping("/admin/panel")
    public String getAdminPanel(Model theModel){
        return "admin/adminPanel";
    }

    @RequestMapping("/admin/apriori/{id}")
    public String getApriori(@PathVariable Long id, Model theModel){

        // getting product
        Product theProduct = productService.findById(id);
        theModel.addAttribute("product", theProduct);

        Map<Product, Apriori> aprioriMap;

        aprioriMap = cartItemService.getAssociationRules(theProduct);

        theModel.addAttribute("supportMap", aprioriMap);

        return "admin/apriori-result";
    }
}
