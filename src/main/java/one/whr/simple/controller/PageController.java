package one.whr.simple.controller;

import one.whr.simple.constant.MessageCode;
import one.whr.simple.dto.response.MessageResponse;
import one.whr.simple.dto.response.PostPageResponse;
import one.whr.simple.entity.Category;
import one.whr.simple.entity.projection.PostProjection;
import one.whr.simple.exceptions.CategoryNotFoundException;
import one.whr.simple.service.CategoryService;
import one.whr.simple.service.PostService;
import one.whr.simple.service.TagService;
import one.whr.simple.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@RequestMapping("/api/page")
public class PageController {
    @Autowired
    PostService postService;

    @Autowired
    TagService tagService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserService userService;

    @GetMapping("/post")
    ResponseEntity<?> getPostPage(@RequestParam int pageNo, @RequestParam(defaultValue = "6") int pageSize) {
        Page<PostProjection> postPage = postService.getPaginatedPosts(pageNo, pageSize);
        return ResponseEntity.ok().body(new PostPageResponse(MessageCode.SUCCESSFUL, "Page query successful", postPage.toList(), postPage.getTotalPages()));
    }

    @GetMapping("/post/category")
    ResponseEntity<?> getPostPageByCategory(@RequestParam Long categoryId, @RequestParam int pageNo, @RequestParam(defaultValue = "6") int pageSize) {
        try {
            Category category = categoryService.findById(categoryId);
            Page<PostProjection> postPage = postService.getPaginatedPostsByCategory(pageNo, pageSize, category);
            return ResponseEntity.ok().body(new PostPageResponse(MessageCode.SUCCESSFUL, "Page query successful", postPage.toList(), postPage.getTotalPages()));

        } catch (CategoryNotFoundException e) {
            return ResponseEntity.ok().body(new MessageResponse(MessageCode.CATEGORY_NOT_FOUND, "cannot find category"));
        }
    }

    @GetMapping("/post/tag")
    ResponseEntity<?> getPostPageByTag(@RequestParam Long tagId, @RequestParam int pageNo, @RequestParam(defaultValue = "6") int pageSize) {
        Page<PostProjection> postPage = postService.getPaginatedPostsByTagId(pageNo, pageSize, tagId);
        return ResponseEntity.ok().body(new PostPageResponse(MessageCode.SUCCESSFUL, "Page query successful", postPage.toList(), postPage.getTotalPages()));
    }
}
