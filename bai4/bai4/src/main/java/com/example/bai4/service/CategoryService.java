package com.example.bai4.service;

import com.example.bai4.model.Category;
import com.example.bai4.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.annotation.PostConstruct;
import java.util.List;

@Service
@Transactional
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    
    @PostConstruct
    public void initData() {
        if (categoryRepository.count() == 0) {
            categoryRepository.save(new Category(null, "Electronics"));
            categoryRepository.save(new Category(null, "Clothing"));
            categoryRepository.save(new Category(null, "Books"));
            categoryRepository.save(new Category(null, "Food"));
            categoryRepository.save(new Category(null, "Furniture"));
        }
    }
    
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }
    
    public Category get(int id) {
        return categoryRepository.findById(id).orElse(null);
    }
    
    public void add(Category category) {
        categoryRepository.save(category);
    }
    
    public void update(Category editCategory) {
        Category find = get(editCategory.getId());
        if (find != null) {
            find.setName(editCategory.getName());
            categoryRepository.save(find);
        }
    }
    
    public void delete(int id) {
        categoryRepository.deleteById(id);
    }
}
