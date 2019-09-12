package com.master.webshop.services;


import com.master.webshop.model.Category;

import java.util.List;

public interface CategoryService {

    public List<Category> findAll();

    public Category findById(int theId);

    public void save(Category theCategory);

    public void deleteById(int theId);

}
