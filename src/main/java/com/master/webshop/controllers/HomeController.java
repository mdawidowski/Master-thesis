package com.master.webshop.controllers;

import com.master.webshop.model.*;
import com.master.webshop.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    ProductService productService;

    @Autowired
    OrderService orderService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CartItemService cartItemService;

    @Autowired
    AssociationService associationService;

    @Autowired
    CategoryService categoryService;

    @RequestMapping(value= {"/home/index"}, method= RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView model;
        model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());

        // create users shopping cart if he doesn't have one
        final String CREATE_NEW_SHOPPING_CART = "INSERT INTO shopping_cart (id, grand_total, user_id) SELECT  (SELECT MAX(id)+1 FROM public.shopping_cart), 0, ? WHERE NOT EXISTS (SELECT 1 FROM shopping_cart WHERE user_id=?);";
        jdbcTemplate.update(CREATE_NEW_SHOPPING_CART, user.getId(), user.getId());
        
        List<Order> orderList = orderService.findByUser(user);
        List<CartItem> cartItemList = new ArrayList<>();
        for (int i = 0; i < orderList.size(); i++) {
            cartItemList.addAll(cartItemService.findByOrder(orderList.get(i)));
        }
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < cartItemList.size(); i++) {
            productList.add(cartItemList.get(i).getProduct());
        }
        Product boughtProduct = new Product();

        if (orderList.size() > 1){
            boughtProduct = productService.randomListOfProducts(productList, 1).get(0);
        }

        List<Product> productsYouMayAlsoLike = new ArrayList<>();
        if (orderList.size() > 1){
            List<Association> associationList = associationService.findBySelectedProductOrderByOccurences(boughtProduct);
            for (int i = 0; i < 6; i++) {
                productsYouMayAlsoLike.add(associationList.get(i).getAssociatedProduct());
            }
            model.addObject("productsYouMayAlsoLike", productsYouMayAlsoLike);
        }

        // add all needed objects to the model
        model.addObject("categories", categoryService.findAll());
        model.addObject("boughtProduct", boughtProduct);
        model.addObject("products", productService.getFiveRandomProducts());
        model.addObject("username", user.getUsername().toUpperCase());
        model.setViewName("home/index");
        return model;
    }
}
