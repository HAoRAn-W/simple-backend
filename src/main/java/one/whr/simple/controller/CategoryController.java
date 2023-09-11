package one.whr.simple.controller;

import lombok.extern.slf4j.Slf4j;
import one.whr.simple.constant.MessageCode;
import one.whr.simple.dto.request.AddCategoryRequest;
import one.whr.simple.dto.request.UpdateCategoryRequest;
import one.whr.simple.dto.response.CategoryListResponse;
import one.whr.simple.dto.response.MessageResponse;
import one.whr.simple.entity.Category;
import one.whr.simple.exceptions.CategoryNotFoundException;
import one.whr.simple.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@Slf4j
public class CategoryController {

    CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    ResponseEntity<?> getAllCategories() {
        List<Category> categories = categoryService.findAll();
        return ResponseEntity.ok().body(new CategoryListResponse(MessageCode.SUCCESSFUL, "Get category list successful", categories));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<?> addCategory(@RequestBody AddCategoryRequest request) {
        Category category = new Category(request.getName(), request.getCoverUrl());
        categoryService.addCategory(category);
        return ResponseEntity.ok().body(new MessageResponse(MessageCode.SUCCESSFUL, "Add category successful"));
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<?> updateCategory(@RequestBody UpdateCategoryRequest request) throws CategoryNotFoundException {
        Category category = categoryService.findById(request.getId());
        category.setName(request.getName());
        category.setCoverUrl(request.getCoverUrl());
        categoryService.addCategory(category);
        return ResponseEntity.ok().body(new MessageResponse(MessageCode.SUCCESSFUL, "Update category successful"));
    }

    @GetMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<?> removeCategory(@RequestParam Long categoryId) throws CategoryNotFoundException {
        categoryService.removeCategory(categoryId);
        return ResponseEntity.ok().body(new MessageResponse(MessageCode.SUCCESSFUL, "Delete category successful"));
    }


}
