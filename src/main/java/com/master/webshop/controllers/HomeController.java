package com.master.webshop.controllers;

import com.master.webshop.model.CartItem;
import com.master.webshop.model.Order;
import com.master.webshop.model.Product;
import com.master.webshop.model.User;
import com.master.webshop.services.CartItemService;
import com.master.webshop.services.OrderService;
import com.master.webshop.services.ProductService;
import com.master.webshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

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

    List<Product> randomProductsList = new ArrayList<Product>() {};

    @RequestMapping(value= {"/home/index"}, method= RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView model;
        model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());

        // create users shopping cart if he doesn't have one
        final String CREATE_NEW_SHOPPING_CART = "INSERT IGNORE INTO `shopping_cart`\n" +
                "SET `grand_total` = 0,\n" +
                "`user_id` = ?;\n";
        jdbcTemplate.update(CREATE_NEW_SHOPPING_CART, user.getId());


        // get list of user orders
        List<Order> listOfOrdersByUser = orderService.findByUser(user);

        // get list of products bought by user
        List<CartItem> listOfCartItemsByUser = new ArrayList<>();

        for (int i = 0; i < listOfOrdersByUser.size(); i++) {
            listOfCartItemsByUser.addAll(cartItemService.findByOrder(listOfOrdersByUser.get(i)));
        }

        List<Product> listOfProductsByUser = new ArrayList<>();
        for (int i = 0; i < listOfOrdersByUser.size(); i++) {
            listOfProductsByUser.add(listOfCartItemsByUser.get(i).getProduct());
        }

        List<Product> boughtProduct = productService.randomListOfProducts(listOfProductsByUser, 1);

        // add all needed objects to the model
        model.addObject("productsYouMayAlsoLike", cartItemService.getAssociationRules(boughtProduct.get(0)));
        model.addObject("products", productService.getFiveRandomProducts());
        model.addObject("boughtProduct",  boughtProduct);
        model.addObject("username", user.getUsername().toUpperCase());
        model.setViewName("home/index");
        return model;
    }
}
