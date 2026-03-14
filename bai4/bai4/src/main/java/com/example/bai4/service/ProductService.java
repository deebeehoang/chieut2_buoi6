package com.example.bai4.service;

import com.example.bai4.model.Product;
import com.example.bai4.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryService categoryService;
    
    @PostConstruct
    public void initData() {
        if (productRepository.count() == 0) {
            // Initialize sample data
            Product p1 = new Product();
            p1.setName("Laptop 1");
            p1.setPrice(30000L);
            p1.setImage("laptop.jpg");
            p1.setCategory(categoryService.get(1)); // Get Electronics
            productRepository.save(p1);
        }
    }
    
    public List<Product> getAll() {
        return productRepository.findAll();
    }
    
    public Product get(int id) {
        return productRepository.findById(id).orElse(null);
    }
    
    public void add(Product newProduct) {
        productRepository.save(newProduct);
    }
    
    public void update(Product editProduct) {
        Product find = get(editProduct.getId());
        if (find != null) {
            find.setName(editProduct.getName());
            find.setPrice(editProduct.getPrice());
            find.setCategory(editProduct.getCategory());
            if (editProduct.getImage() != null && !editProduct.getImage().isEmpty()) {
                find.setImage(editProduct.getImage());
            }
            productRepository.save(find);
        }
    }
    
    public void delete(int id) {
        productRepository.deleteById(id);
    }
    
    public void updateImage(Product newProduct, MultipartFile imageProduct) {
        if (!imageProduct.isEmpty()) {
            try {
                String contentType = imageProduct.getContentType();
                if (contentType != null && !contentType.startsWith("image/")) {
                    throw new IllegalArgumentException("Tệp tải lên không phải là hình ảnh!");
                }
                
                Path dirImages = Paths.get("static/images");
                if (!Files.exists(dirImages)) {
                    Files.createDirectories(dirImages);
                }
                
                String newFileName = UUID.randomUUID() + "_" + imageProduct.getOriginalFilename();
                Path pathFileUpload = dirImages.resolve(newFileName);
                Files.copy(imageProduct.getInputStream(), pathFileUpload, StandardCopyOption.REPLACE_EXISTING);
                newProduct.setImage(newFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
