package com.master.webshop.services;


import com.master.webshop.model.Order;
import com.master.webshop.model.ShoppingCart;
import com.master.webshop.model.User;

import java.util.List;

public interface OrderService {
	Order createOrder(ShoppingCart shoppingCart, User user);

	Order findById(Long id);

    List<Order> findAll();
}
