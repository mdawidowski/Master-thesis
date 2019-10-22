package com.master.webshop.controllers;

import com.master.webshop.model.Apriori;
import com.master.webshop.model.CartItem;
import com.master.webshop.model.Product;
import com.master.webshop.services.CartItemService;
import com.master.webshop.services.OrderService;
import com.master.webshop.services.ProductService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        return "adminPanel";
    }

    @RequestMapping("/admin/apriori/{id}")
    public String getApriori(@PathVariable Long id, Model theModel){

        // getting product
        Product theProduct = productService.findById(id);
        theModel.addAttribute("product", theProduct);

        // creating list of cartItems that contain chosen product
        List<CartItem> cartItemList = cartItemService.findByProduct(theProduct);
        theModel.addAttribute("cartItemList", cartItemList);

        // creating list of cartItems that contain all products from all orders that contain chosen product
        List<CartItem> cartItemsByOrders = new ArrayList<>();
        for (int i = 0; i < cartItemList.size(); i++) {
            cartItemsByOrders.addAll(cartItemService.findByOrder(cartItemList.get(i).getOrder()));
        }

        // getting amount of all orders
        int amountOfAllOrders = orderService.findAll().size();

        // hashmap that contain association rules
        Apriori apriori;
        Apriori newApriori;
        Map<Product, Apriori> aprioriMap = new HashMap<>();

        // support frq(X,Y)/totalOrders
        // confidence frq(X,Y)/frq(X)
        // lift support/support(X)*(support(Y)

        for (int i = 0; i < cartItemsByOrders.size(); i++) {
            if (cartItemsByOrders.get(i).getProduct() != theProduct){

                List<CartItem> cartItemsListForProductY = cartItemService.findByProduct(cartItemsByOrders.get(i).getProduct());

                if (aprioriMap.containsKey(cartItemsByOrders.get(i).getProduct())){
                    apriori = new Apriori(
                            1.0/amountOfAllOrders,
                            1.0/cartItemList.size(),
                            (1.0/amountOfAllOrders)/(((double)cartItemList.size()/amountOfAllOrders) * ((double)cartItemsListForProductY.size()/amountOfAllOrders)));

                    newApriori = new Apriori(aprioriMap.get(cartItemsByOrders.get(i).getProduct()).getSupport(), aprioriMap.get(cartItemsByOrders.get(i).getProduct()).getConfidence(), aprioriMap.get(cartItemsByOrders.get(i).getProduct()).getLift());
                    newApriori.mergeApriori(apriori);

                    aprioriMap.put(cartItemsByOrders.get(i).getProduct(), newApriori);
                } else {
                    apriori = new Apriori(
                            1.0/amountOfAllOrders,
                            1.0/cartItemList.size(),
                            (1.0/amountOfAllOrders)/(((double)cartItemList.size()/amountOfAllOrders) * ((double)cartItemsListForProductY.size()/amountOfAllOrders)));
                    aprioriMap.put(cartItemsByOrders.get(i).getProduct(), apriori);
                }
            }
        }


        theModel.addAttribute("cartItemsByOrders", cartItemsByOrders);
        theModel.addAttribute("supportMap", aprioriMap);

        return "admin/apriori-result";
    }
}
