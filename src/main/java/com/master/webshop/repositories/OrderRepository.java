package com.master.webshop.repositories;


import com.master.webshop.model.Order;
import com.master.webshop.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {

    List<Order> findByUser(User user);
}

