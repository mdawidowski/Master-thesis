package com.master.webshop.services;


import com.master.webshop.model.*;

import java.util.List;
import java.util.Map;

public interface CartItemService {
	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
	
	CartItem updateCartItem(CartItem cartItem);
	
	CartItem addProductToCartItem(Product product, User user, int qty);
	
	CartItem findById(Long id);

	List<CartItem> findAll();
	
	void removeCartItem(CartItem cartItem);

    CartItem save(CartItem cartItem);

    List<CartItem> findByProduct(Product product);

    List<CartItem> findByOrder(Order order);

	double countAll();

	double countAllByProduct(Product product);

//	Map<Product, Apriori> getAssociationRules(Product product);
}
