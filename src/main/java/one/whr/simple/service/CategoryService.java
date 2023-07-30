package one.whr.simple.service;

import one.whr.simple.entity.Category;
import one.whr.simple.entity.Post;
import one.whr.simple.exceptions.CategoryNotFoundException;
import one.whr.simple.repository.CategoryRepository;
import one.whr.simple.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PostRepository postRepository;

    private static volatile Category defaultCategory;

    public Category findById(Long id) throws CategoryNotFoundException {
        return categoryRepository.findById(id).orElseThrow(() ->  new CategoryNotFoundException("Cannot find category"));
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

    public void removeCategory(Long categoryId) throws CategoryNotFoundException {
        if (categoryId == 1L) {
            // cannot delete default category
            return;
        }
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException("can't find category"));

        // before deleting category, move all posts under this category to default category
        List<Post> posts = category.getPosts();
        for (Post post : posts) {
            post.setCategory(getDefaultCategory());
        }
        postRepository.saveAll(posts);
        categoryRepository.deleteById(categoryId);
    }

    public Category getDefaultCategory() throws CategoryNotFoundException {
        if (defaultCategory == null) {
            synchronized (this) {
                if (defaultCategory == null) {
                    defaultCategory = categoryRepository.findById(1L).orElseThrow(() -> new CategoryNotFoundException("can't find default category"));
                }
            }
        }
        return defaultCategory;
    }
}
