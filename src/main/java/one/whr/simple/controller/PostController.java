package one.whr.simple.controller;

import lombok.extern.slf4j.Slf4j;
import one.whr.simple.constant.MessageCode;
import one.whr.simple.dto.request.PostCreationRequest;
import one.whr.simple.dto.response.MessageResponse;
import one.whr.simple.dto.response.PostPageResponse;
import one.whr.simple.entity.Category;
import one.whr.simple.entity.Post;
import one.whr.simple.exceptions.CategoryNotFoundException;
import one.whr.simple.service.CategoryService;
import one.whr.simple.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/post")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@Slf4j
public class PostController {

    @Autowired
    PostService postService;

    @Autowired
    CategoryService categoryService;


    @GetMapping("/{pageNo}")
    ResponseEntity<?> getPageList(@PathVariable int pageNo) {
        Page<Post> postPage = postService.getPaginatedPosts(pageNo, 1);
        return ResponseEntity.ok().body(new PostPageResponse(MessageCode.SUCCESSFUL, "Page query successful",postPage.toList()));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<?> addNewPost(@RequestBody PostCreationRequest request) {
        try{
            Post newPost = new Post(request.getTitle(), request.getDescription(), request.getContent());
            newPost.setCreatedTime(LocalDateTime.now());

            Category category = categoryService.findById(request.getCategoryId()).orElseThrow(() -> new CategoryNotFoundException("Can't find post"));
            newPost.setCategory(category);

            postService.addNewPost(newPost);

        } catch (CategoryNotFoundException e) {
            return ResponseEntity.ok().body(new MessageResponse(MessageCode.CATEGORY_NOT_FOUND, "Cannot find category"));
        }

        return ResponseEntity.ok()
                .body(new MessageResponse(MessageCode.SUCCESSFUL, "Add new post success"));
    }
}
