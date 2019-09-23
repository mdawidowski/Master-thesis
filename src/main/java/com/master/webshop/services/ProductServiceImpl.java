package com.master.webshop.services;

import com.master.webshop.model.Product;
import com.master.webshop.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository theProductRepository) {
        productRepository = theProductRepository;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {

        Optional<Product> result = productRepository.findById(id);

        Product theProduct = null;

        if (result.isPresent()) {
            theProduct = result.get();
        }
        else {
            // we didn't find the category
            throw new RuntimeException("Did not find category id - " + id);
        }

        return theProduct;
    }

    @Override
    public void save(Product theProduct) {
        productRepository.save(theProduct);
    }

    @Override
    public void deleteById(Long theId) {
        productRepository.deleteById(theId);
    }

    public List<Product> findAllByOrderByCategory(){ return productRepository.findAllByOrderByCategory(); }
}
