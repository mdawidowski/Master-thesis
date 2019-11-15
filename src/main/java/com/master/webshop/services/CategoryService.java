package com.master.webshop.services;


import com.master.webshop.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> findAll();

    Category findById(int theId);

    void save(Category theCategory);

    void deleteById(int theId);

}
