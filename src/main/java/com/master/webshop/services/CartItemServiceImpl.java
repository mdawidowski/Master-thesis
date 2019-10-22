package com.master.webshop.services;


import com.master.webshop.model.*;
import com.master.webshop.repositories.CartItemRepository;
import com.master.webshop.repositories.ProductToCartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CartItemServiceImpl implements CartItemService{
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private ProductToCartItemRepository productToCartItemRepository;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private OrderService orderService;

	public List<CartItem> findByShoppingCart(ShoppingCart shoppingCart) {
		return cartItemRepository.findByShoppingCart(shoppingCart);
	}
	
	public CartItem updateCartItem(CartItem cartItem) {
		BigDecimal bigDecimal = new BigDecimal(cartItem.getProduct().getPrice()).multiply(new BigDecimal(cartItem.getQty()));
		
		bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
		cartItem.setSubtotal(bigDecimal);
		
		cartItemRepository.save(cartItem);
		
		return cartItem;
	}
	
	public CartItem addProductToCartItem(Product product, User user, int qty) {
		List<CartItem> cartItemList = findByShoppingCart(user.getShoppingCart());
		
		for (CartItem cartItem : cartItemList) {
			if(product.getId() == cartItem.getProduct().getId()) {
				cartItem.setQty(cartItem.getQty()+qty);
				cartItem.setSubtotal(new BigDecimal(product.getPrice()).multiply(new BigDecimal(qty)));
				cartItemRepository.save(cartItem);
				return cartItem;
			}
		}
		
		CartItem cartItem = new CartItem();
		cartItem.setShoppingCart(user.getShoppingCart());
		cartItem.setProduct(product);
		
		cartItem.setQty(qty);
		cartItem.setSubtotal(new BigDecimal(product.getPrice()).multiply(new BigDecimal(qty)));
		cartItem = cartItemRepository.save(cartItem);
		
		ProductToCartItem productToCartItem = new ProductToCartItem();
		productToCartItem.setProduct(product);
		productToCartItem.setCartItem(cartItem);
		productToCartItemRepository.save(productToCartItem);
		
		return cartItem;
	}
	
	public CartItem findById(Long id) {
		Optional<CartItem> result = cartItemRepository.findById(id);

		CartItem cartItem= null;

		if (result.isPresent()) {
			cartItem = result.get();
		}
		else {
			// we didn't find the category
			throw new RuntimeException("Did not find category id - " + id);
		}

		return cartItem;
	}
	
	public void removeCartItem(CartItem cartItem) {
		productToCartItemRepository.deleteByCartItem(cartItem);
		cartItemRepository.delete(cartItem);
	}

	@Override
	public CartItem save(CartItem cartItem) {
		return cartItemRepository.save(cartItem);
	}


	@Override
	public List<CartItem> findByProduct(Product product){
		return cartItemRepository.findByProduct(product);
	}

	@Override
	public List<CartItem> findByOrder(Order order) {
		return cartItemRepository.findByOrder(order);
	}

	@Override
	public Map<Product, Apriori> getAssociationRules(Product product) {

		// creating list of cartItems that contain chosen product
		List<CartItem> cartItemList = cartItemService.findByProduct(product);

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
			if (cartItemsByOrders.get(i).getProduct() != product){

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

		return aprioriMap;
	}


}
