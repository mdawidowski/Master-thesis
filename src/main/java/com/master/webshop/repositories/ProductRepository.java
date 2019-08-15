package com.master.webshop.repositories;

import com.master.webshop.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer> {


}
