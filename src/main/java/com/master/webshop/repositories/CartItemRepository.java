package com.master.webshop.repositories;


import com.master.webshop.model.CartItem;
import com.master.webshop.model.Order;
import com.master.webshop.model.Product;
import com.master.webshop.model.ShoppingCart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CartItemRepository extends CrudRepository<CartItem, Long> {
	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);

	List<CartItem> findByProduct(Product product);

	List<CartItem> findByOrder(Order order);
}

