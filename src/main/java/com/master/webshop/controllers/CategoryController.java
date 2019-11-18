package com.master.webshop.controllers;

import com.master.webshop.model.Category;
import com.master.webshop.services.CategoryService;
import com.master.webshop.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;
    // add mapping for "/list"

    @GetMapping("/list")
    public String listCategories(Model theModel) {

        // get categories from db
        List<Category> theCategories = categoryService.findAll();

        // add to the spring model
        theModel.addAttribute("categories", theCategories);

        return "categories/list-categories";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel) {

        // create model attribute to bind form data
        Category theCategory= new Category();

        theModel.addAttribute("category", theCategory);

        return "categories/category-form";
    }

    @PostMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("CategoryId") int theId,
                                    Model theModel) {

        // get the category from the service
        Category theCategory = categoryService.findById(theId);

        // set category as a model attribute to pre-populate the form
        theModel.addAttribute("category", theCategory);

        // send over to our form
        return "categories/category-form";
    }

    @RequestMapping("category/{id}")
    public String showProduct(@PathVariable int id, Model model){

        model.addAttribute("products", productService.findAllByCategory(categoryService.findById(id)));
        model.addAttribute("category", categoryService.findById(id));
        return "categories/category-show";
    }


    @PostMapping("/save")
    public String saveCategory(@ModelAttribute("category") Category theCategory) {

        // save the category
        categoryService.save(theCategory);

        // use a redirect to prevent duplicate submissions
        return "redirect:/categories/list";
    }


    @PostMapping("/delete")
    public String delete(@RequestParam("categoryId") int theId) {

        // delete the category
        categoryService.deleteById(theId);

        // redirect to /categories/list
        return "redirect:/categories/list";

    }
}
