package one.whr.simple.service;

import one.whr.simple.entity.Category;
import one.whr.simple.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }
}
