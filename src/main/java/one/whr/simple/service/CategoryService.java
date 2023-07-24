package one.whr.simple.service;

import one.whr.simple.entity.Category;
import one.whr.simple.exceptions.CategoryNotFoundException;
import one.whr.simple.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public Category findById(Long id) throws CategoryNotFoundException {
        return categoryRepository.findById(id).orElseThrow(() ->  new CategoryNotFoundException("Cannot find category"));
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public void addCategory(Category category) {
        categoryRepository.save(category);
    }
}
