package com.master.webshop.repositories;

import com.master.webshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByOrderByCategory();

}
