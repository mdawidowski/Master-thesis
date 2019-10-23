package com.master.webshop.services;

import com.master.webshop.model.Product;
import com.master.webshop.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ProductServiceImpl implements ProductService{

    private ProductRepository productRepository;

    private List<Product> randomProductsList;

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

    public List<Product> getFiveRandomProducts(){
        List<Product> theProducts = findAllByOrderByCategory();
        randomProductsList = randomListOfProducts(theProducts, 5);
        return randomProductsList;
    }

    @Override
    public List<Product> randomListOfProducts(List<Product> productList, int size){
        Random generator = new Random();
        randomProductsList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            randomProductsList.add(productList.get(generator.nextInt(productList.size())));
        }
        return randomProductsList;
    }
}
