package com.master.webshop.controllers;

import com.master.webshop.model.Association;
import com.master.webshop.model.Product;
import com.master.webshop.services.AssociationService;
import com.master.webshop.services.CartItemService;
import com.master.webshop.services.OrderService;
import com.master.webshop.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Controller
public class AdminPanelController {
//    creating mapping

    @Autowired
    ProductService productService;

    @Autowired
    AssociationService associationService;

    @Autowired
    CartItemService cartItemService;

    @Autowired
    OrderService orderService;

    @GetMapping("/admin/panel")
    public String getAdminPanel() {
        return "admin/adminPanel";
    }

    @RequestMapping("/admin/apriori/{id}")
    public String getApriori(@PathVariable Long id, Model theModel){

        // getting product
        Product theProduct = productService.findById(id);
        List<Association> associations = associationService.findBySelectedProductOrderByOccurences(theProduct);
        Map<Product, Double> productIntegerMap = new HashMap<>();

        for (int i = 0; i < associations.size(); i++) {
            productIntegerMap.put(associations.get(i).getAssociatedProduct(), cartItemService.countAllByProduct(associations.get(i).getAssociatedProduct()));
        }

        theModel.addAttribute("productsMap", productIntegerMap);
        theModel.addAttribute("product", theProduct);
        theModel.addAttribute("allOrders", orderService.countAll());
        theModel.addAttribute("cartItemListSize", (double)associations.size());
        theModel.addAttribute("associations", associations);
        return "admin/apriori-result";
    }
}
