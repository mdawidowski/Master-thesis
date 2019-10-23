package com.master.webshop.services;

import com.master.webshop.model.*;
import com.master.webshop.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CartItemService cartItemService;
	
	public synchronized Order createOrder(ShoppingCart shoppingCart,
										  User user) {
		Order order = new Order();

		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		
		for(CartItem cartItem : cartItemList) {
			cartItem.setOrder(order);
		}
		
		order.setCartItemList(cartItemList);
		order.setOrderDate(Calendar.getInstance().getTime());
		order.setOrderTotal(shoppingCart.getGrandTotal());
		order.setUser(user);
		order = orderRepository.save(order);
		
		return order;
	}

	@Override
	public Order findById(Long id) {
		Optional<Order> result = orderRepository.findById(id);

		Order theOrder = null;

		if (result.isPresent()) {
			theOrder = result.get();
		}
		else {
			// we didn't find the category
			throw new RuntimeException("Did not find order id - " + id);
		}

		return theOrder;
	}

	@Override
	public List<Order> findAll() {
		return (List<Order>) orderRepository.findAll();
	}

	@Override
	public List<Order> findByUser(User user){
		return orderRepository.findByUser(user);
	}
}
