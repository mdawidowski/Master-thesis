package com.master.webshop.controllers;

import com.master.webshop.model.*;
import com.master.webshop.services.CartItemService;
import com.master.webshop.services.OrderService;
import com.master.webshop.services.ShoppingCartService;
import com.master.webshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CheckoutController {

	
	@Autowired
	private UserService userService;

	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private OrderService orderService;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@RequestMapping("/checkout")
	public String checkout(@RequestParam("id") Long cartId,
                           @RequestParam(value = "missingRequiredField", required = false) boolean missingRequiredField, Model model,
                           Principal principal) {
		User user = userService.findUserByUsername(principal.getName());

		if (cartId != user.getShoppingCart().getId()) {
			return "badRequestPage";
		}

		List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());

		if (cartItemList.size() == 0) {
			model.addAttribute("emptyCart", true);
			return "forward:/shoppingCart/cart";
		}

		ShoppingCart shoppingCart = user.getShoppingCart();

		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", user.getShoppingCart());

		if (missingRequiredField) {
			model.addAttribute("missingRequiredField", true);
		}
		return "orders/checkout";

	}

	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String checkoutPost(Principal principal, Model model) {
		ShoppingCart shoppingCart = userService.findUserByUsername(principal.getName()).getShoppingCart();

		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		model.addAttribute("cartItemList", cartItemList);

		User user = userService.findUserByUsername(principal.getName());

		final String query = "UPDATE association SET occurences = occurences + 1 WHERE associated_product_id = ? and selected_product_id = ?;\n" +
				"INSERT INTO association (occurences, associated_product_id, selected_product_id)\n" +
				"       SELECT 1, ?, ? \n" +
				"       WHERE NOT EXISTS (SELECT 1 FROM association WHERE associated_product_id = ? and selected_product_id = ?);";

		for (int i = 0; i < cartItemList.size(); i++) {
			for (int j = 0; j < cartItemList.size(); j++) {
				if (i != j){
					jdbcTemplate.update(query, cartItemList.get(i).getProduct().getId(), cartItemList.get(j).getProduct().getId(),  cartItemList.get(i).getProduct().getId(), cartItemList.get(j).getProduct().getId(), cartItemList.get(i).getProduct().getId(), cartItemList.get(j).getProduct().getId());
				}
			}
		}

		Order order = orderService.createOrder(shoppingCart, user);

		shoppingCartService.clearShoppingCart(shoppingCart);

		return "orders/orderSubmittedPage";
	}

	@RequestMapping("order/{id}")
	public String showProduct(@PathVariable Long id, Model model){
		model.addAttribute("order", orderService.findById(id));
		return "orders/order-show";
	}

	@GetMapping("orders/list")
	public String listProducts(Model theModel) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByUsername(auth.getName());

		List<Order> orderList = orderService.findAll();
		// get orders from db
		List<Order> theOrders =
				//Create a Stream from the orderList
				orderList.stream().
						//filter the element to select only those with user_id
								filter(p -> p.getUser().getId() == user.getId()).
						//put those filtered elements into a new List.
								collect(Collectors.toList());
		// add to the spring model
		theModel.addAttribute("orders", theOrders);
		return "orders/user-orders";
	}

	@GetMapping("admin/all-orders")
	public String listAllOrders(Model theModel) {
		List<Order> orderList = orderService.findAll();

		// add to the spring model
		theModel.addAttribute("orders", orderList);

		return "orders/all-orders";
	}

	@GetMapping("admin/all-cartitems")
	public String listAllCartItems(Model theModel) {
		List<CartItem> orderList = cartItemService.findAll();

		// add to the spring model
		theModel.addAttribute("orders", orderList);

		return "orders/all-cartitems";
	}
}
