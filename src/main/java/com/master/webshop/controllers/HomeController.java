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
        Random random = new Random();
        int item = random.nextInt(cartItemList.size());
        Product boughtProduct = cartItemList.get(item).getProduct();

        List<Association> productsYouMayAlsoLike = associationService.findBySelectedProductOrderByOccurencesLimit5(boughtProduct);

        // add all needed objects to the model
        model.addObject("productsYouMayAlsoLike", productsYouMayAlsoLike);
        model.addObject("boughtProduct", boughtProduct);
        model.addObject("products", productService.getFiveRandomProducts());
        model.addObject("username", user.getUsername().toUpperCase());
        model.setViewName("home/index");
        return model;
    }
}