package com.master.webshop.controllers;

import com.master.webshop.model.Product;
import com.master.webshop.model.User;
import com.master.webshop.repositories.ProductRepository;
import com.master.webshop.services.ProductService;
import com.master.webshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    ProductService productService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    List<Product> randomProductsList = new ArrayList<Product>() {};

    @RequestMapping(value= {"/", "/login"}, method=RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView model = new ModelAndView();

        model.setViewName("user/login");
        return model;
    }

    @RequestMapping(value= {"/signup"}, method=RequestMethod.GET)
    public ModelAndView signup() {
        ModelAndView model = new ModelAndView();
        User user = new User();
        model.addObject("user", user);
        model.setViewName("user/signup");

        return model;
    }

    @RequestMapping(value= {"/signup"}, method=RequestMethod.POST)
    public ModelAndView createUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView model = new ModelAndView();
        User userExists = userService.findUserByUsername(user.getUsername());

        if(userExists != null) {
            bindingResult.rejectValue("username", "error.user", "This username already exists!");
        }
        if(bindingResult.hasErrors()) {
            model.setViewName("user/signup");
        } else {
            userService.saveUser(user);
            model.addObject("msg", "User has been registered successfully!");
            model.addObject("user", new User());
            model.setViewName("user/signup");
        }

        return model;
    }

    @RequestMapping(value= {"/home/index"}, method=RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView model;
        model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        List<Product> theProducts = productService.findAllByOrderByCategory();


        // create users shopping cart if he doesn't have one
        final String CREATE_NEW_SHOPPING_CART = "INSERT IGNORE INTO `shopping_cart`\n" +
                "SET `grand_total` = 0,\n" +
                "`user_id` = ?;\n";
        jdbcTemplate.update(CREATE_NEW_SHOPPING_CART, user.getId());
        randomProductsList = randomListOfProducts(theProducts);
        model.addObject("products", randomProductsList);
        model.addObject("username", user.getUsername().toUpperCase());
        model.setViewName("home/index");
        return model;
    }

    @RequestMapping(value= {"/access_denied"}, method=RequestMethod.GET)
    public ModelAndView accessDenied() {
        ModelAndView model = new ModelAndView();
        model.setViewName("errors/access_denied");
        return model;
    }

    List<Product> randomListOfProducts(List<Product> productList){
        Random generator = new Random();
        randomProductsList.clear();

        for (int i = 0; i < 5; i++) {
            randomProductsList.add(productList.get(generator.nextInt(productList.size())));
        }
        return randomProductsList;
    }
}