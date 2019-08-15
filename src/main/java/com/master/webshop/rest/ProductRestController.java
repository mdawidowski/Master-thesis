package com.master.webshop.rest;

import com.master.webshop.model.Product;
import com.master.webshop.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ProductRestController {

    @Autowired
    ProductRepository repository;

    @Autowired
    public ProductRestController(ProductRepository theRepository){
        repository = theRepository;
    }

    @GetMapping("/api/product")
    public Iterable<Product> findAllItems(){
        return repository.findAll();
    }

}
