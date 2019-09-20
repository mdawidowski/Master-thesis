package com.master.webshop.controllers;

import com.master.webshop.model.Category;
import com.master.webshop.model.Product;
import com.master.webshop.services.CategoryService;
import com.master.webshop.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;

    private CategoryService categoryService;

    public ProductController(ProductService theProductService, CategoryService theCategoryService) {
        productService = theProductService;
        categoryService = theCategoryService;
    }

    // add mapping for "/list"

    @GetMapping("/list")
    public String listProducts(Model theModel) {

        // get products from db
        List<Product> theProducts = productService.findAll();

        // add to the spring model
        theModel.addAttribute("products", theProducts);

        return "products/list-products";
    }

    @RequestMapping("product/{id}")
    public String showProduct(@PathVariable Long id, Model model){
        model.addAttribute("product", productService.findById(id));
        return "products/product-show";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel) {

        // create model attribute to bind form data
        Product theProduct= new Product();
        List<Category> categoryList = categoryService.findAll();
        theModel.addAttribute("categoryList", categoryList);
        theModel.addAttribute("product", theProduct);

        return "products/product-form";
    }

//    @PostMapping("/showFormForUpdate")
//    public String showFormForUpdate(@RequestParam("ProductId") Long theId,
//                                    Model theModel) {
//
//        // get the product from the service
//        Product theProduct = productService.findById(theId);
//
//        // set product as a model attribute to pre-populate the form
//        theModel.addAttribute("product", theProduct);
//
//        // send over to our form
//        return "products/product-form";
//    }


    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product theProduct) {

        // save the product
        productService.save(theProduct);

        // use a redirect to prevent duplicate submissions
        return "redirect:/products/list";
    }


    @PostMapping("/delete")
    public String delete(@RequestParam("productId") Long theId) {

        // delete the product
        productService.deleteById(theId);

        // redirect to /products/list
        return "redirect:/products/list";

    }

}
