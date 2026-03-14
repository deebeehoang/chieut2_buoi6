package com.example.bai4.controller;

import com.example.bai4.model.Category;
import com.example.bai4.model.Product;
import com.example.bai4.service.CategoryService;
import com.example.bai4.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    
    @Autowired
    private CategoryService categoryService;
    
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("listproduct", productService.getAll());
        return "product/products";
    }
    
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAll());
        return "product/create";
    }
    
    @PostMapping("/create")
    public String create(@Valid Product newProduct, BindingResult result,
                        @RequestParam("categoryId") int categoryId,
                        @RequestParam(value = "imageProduct", required = false) MultipartFile imageProduct,
                        Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAll());
            return "product/create";
        }
        
        if (imageProduct != null && !imageProduct.isEmpty()) {
            productService.updateImage(newProduct, imageProduct);
        }
        Category selectedCategory = categoryService.get(categoryId);
        newProduct.setCategory(selectedCategory);
        productService.add(newProduct);
        return "redirect:/products";
    }
    
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {
        Product find = productService.get(id);
        if (find == null) {
            return "error/404";
        }
        model.addAttribute("product", find);
        model.addAttribute("categories", categoryService.getAll());
        return "product/edit";
    }
    
    @PostMapping("/edit")
    public String edit(@Valid Product editProduct, BindingResult result,
                      @RequestParam("categoryId") int categoryId,
                      @RequestParam(value = "imageProduct", required = false) MultipartFile imageProduct,
                      Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAll());
            return "product/edit";
        }
        
        if (imageProduct != null && !imageProduct.isEmpty() && imageProduct.getSize() > 0) {
            productService.updateImage(editProduct, imageProduct);
        }
        Category selectedCategory = categoryService.get(categoryId);
        editProduct.setCategory(selectedCategory);
        productService.update(editProduct);
        return "redirect:/products";
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        productService.delete(id);
        return "redirect:/products";
    }
}
