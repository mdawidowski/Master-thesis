package com.master.webshop.services;


import com.master.webshop.model.CartItem;
import com.master.webshop.model.Product;
import com.master.webshop.model.ShoppingCart;
import com.master.webshop.model.User;

import java.util.List;

public interface CartItemService {
	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
	
	CartItem updateCartItem(CartItem cartItem);
	
	CartItem addProductToCartItem(Product product, User user, int qty);
	
	CartItem findById(Long id);
	
	void removeCartItem(CartItem cartItem);

    CartItem save(CartItem cartItem);
}
