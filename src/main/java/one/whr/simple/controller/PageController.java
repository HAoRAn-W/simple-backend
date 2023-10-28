package one.whr.simple.controller;

import one.whr.simple.constant.MessageCode;
import one.whr.simple.dto.response.PostPageResponse;
import one.whr.simple.entity.Category;
import one.whr.simple.entity.projection.PostProjection;
import one.whr.simple.exceptions.CategoryNotFoundException;
import one.whr.simple.service.CategoryService;
import one.whr.simple.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/page")
public class PageController {

    PostService postService;

    CategoryService categoryService;

    @Autowired
    public PageController(PostService postService, CategoryService categoryService) {
        this.postService = postService;
        this.categoryService = categoryService;
    }

    @GetMapping("/post")
    ResponseEntity<?> getPostPage(@RequestParam int pageNo, @RequestParam(defaultValue = "6") int pageSize) {
        Page<PostProjection> postPage = postService.getPaginatedPosts(pageNo, pageSize);
        return ResponseEntity.ok().body(new PostPageResponse(MessageCode.SUCCESSFUL, "Page query successful", postPage.toList(), postPage.getTotalPages()));
    }

    @GetMapping("/post/category")
    ResponseEntity<?> getPostPageByCategory(@RequestParam Long categoryId, @RequestParam int pageNo, @RequestParam(defaultValue = "6") int pageSize)
            throws CategoryNotFoundException {
        Category category = categoryService.findById(categoryId);
        Page<PostProjection> postPage = postService.getPaginatedPostsByCategory(pageNo, pageSize, category);
        return ResponseEntity.ok().body(new PostPageResponse(MessageCode.SUCCESSFUL, "Page query successful", postPage.toList(), postPage.getTotalPages()));

    }

    @GetMapping("/post/tag")
    ResponseEntity<?> getPostPageByTag(@RequestParam Long tagId, @RequestParam int pageNo, @RequestParam(defaultValue = "6") int pageSize) {
        Page<PostProjection> postPage = postService.getPaginatedPostsByTagId(pageNo, pageSize, tagId);
        return ResponseEntity.ok().body(new PostPageResponse(MessageCode.SUCCESSFUL, "Page query successful", postPage.toList(), postPage.getTotalPages()));
    }
}
