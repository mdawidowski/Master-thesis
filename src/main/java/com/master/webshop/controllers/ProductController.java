package com.master.webshop.controllers;

import com.master.webshop.model.Category;
import com.master.webshop.model.Product;
import com.master.webshop.services.CategoryService;
import com.master.webshop.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;


@Controller
public class ProductController {

    private ProductService productService;

    private CategoryService categoryService;

    public ProductController(ProductService theProductService, CategoryService theCategoryService) {
        productService = theProductService;
        categoryService = theCategoryService;
    }

    // add mapping for "/list"

    @GetMapping("products/list")
    public String listProducts(Model theModel) {

        // get products from db
        List<Product> theProducts = productService.findAllByOrderByCategory();

        // add to the spring model
        theModel.addAttribute("products", theProducts);

        return "products/list-products";
    }

    @RequestMapping("/products/product/{id}")
    public String showProduct(@PathVariable Long id, Model model){
        model.addAttribute("product", productService.findById(id));
        return "products/product-show";
    }

    @GetMapping("products/showFormForAdd")
    public String showFormForAdd(Model theModel) {

        // create model attribute to bind form data
        Product theProduct= new Product();
        List<Category> categoryList = categoryService.findAll();
        theModel.addAttribute("categoryList", categoryList);
        theModel.addAttribute("product", theProduct);

        return "products/product-form";
    }

    @GetMapping("products/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable Long id,
                                    Model theModel) {

        // get the product from the service
        Product theProduct = productService.findById(id);

        List<Category> categoryList = categoryService.findAll();
        theModel.addAttribute("categoryList", categoryList);
        theModel.addAttribute("product", theProduct);

        // send over to our form
        return "products/product-form";
    }


    @PostMapping("products/save")
    public String saveProduct(@ModelAttribute("product") Product theProduct) {

        // save the product
        productService.save(theProduct);
        // use a redirect to prevent duplicate submissions
        return "redirect:/products/product/" + theProduct.getId();
    }


    @PostMapping("products/delete")
    public String delete(@RequestParam("productId") Long theId) {

        // delete the product
        productService.deleteById(theId);

        // redirect to /products/list
        return "redirect:/products/list";

    }

}
