package com.master.webshop.controllers;

import com.master.webshop.model.CartItem;
import com.master.webshop.model.Order;
import com.master.webshop.model.Product;
import com.master.webshop.services.CartItemService;
import com.master.webshop.services.ProductService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Component
@Controller
public class AdminPanelController {
//    creating mapping

    ProductService productService;

    CartItemService cartItemService;

    public AdminPanelController(ProductService productService, CartItemService cartItemService) {
        this.productService = productService;
        this.cartItemService = cartItemService;
    }

    @GetMapping("/admin/panel")
    public String getAdminPanel(Model theModel){
        return "adminPanel";
    }

    @RequestMapping("/admin/apriori/{id}")
    public String getApriori(@PathVariable Long id, Model theModel){

        Product theProduct = productService.findById(id);
        theModel.addAttribute("product", theProduct);

        List<CartItem> cartItemList = cartItemService.findByProduct(theProduct);
        theModel.addAttribute("cartItemList", cartItemList);

        List<CartItem> cartItemsByOrders = new ArrayList<>();

        for (int i = 0; i < cartItemList.size(); i++) {
            cartItemsByOrders.addAll(cartItemService.findByOrder(cartItemList.get(i).getOrder()));
        }

        theModel.addAttribute("cartItemsByOrders", cartItemsByOrders);

        return "admin/apriori-result";
    }
}
