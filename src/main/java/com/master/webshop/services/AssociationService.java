package com.master.webshop.services;

import com.master.webshop.model.Association;
import com.master.webshop.model.Product;

import java.util.List;

public interface AssociationService {

    List<Association> findAll();

    List<Association> findBySelectedProductOrderByOccurences(Product product);

    void save(Association association);

}
