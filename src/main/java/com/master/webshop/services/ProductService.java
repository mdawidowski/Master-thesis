package com.master.webshop.services;

import com.master.webshop.model.Category;
import com.master.webshop.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> findAll();

    Product findById(Long id);

    void save(Product theProduct);

    void deleteById(Long theId);

    List<Product> findAllByOrderByCategory();

    List<Product> getFiveRandomProducts();

    List<Product> randomListOfProducts(List<Product> productList, int size);

    List<Product> findAllByCategory(Category category);
}
