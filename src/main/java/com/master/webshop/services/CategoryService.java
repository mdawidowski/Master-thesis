package com.master.webshop.services;


import com.master.webshop.model.Apriori;
import com.master.webshop.model.Category;

import java.util.List;
import java.util.Map;

public interface CategoryService {

    List<Category> findAll();

    Category findById(int theId);

    void save(Category theCategory);

    void deleteById(int theId);

    Map<Category, Apriori> categoryAssociationRules(Category category);

}
