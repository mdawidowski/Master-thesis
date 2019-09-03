package com.master.webshop.services;

import com.master.webshop.model.Product;

import java.util.List;

public interface ProductService {

    public List<Product> findAll();

    public Product findById(int theId);

    public void save(Product theProduct);

    public void deleteById(int theId);

}
