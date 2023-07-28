package one.whr.simple.controller;

import lombok.extern.slf4j.Slf4j;
import one.whr.simple.constant.MessageCode;
import one.whr.simple.dto.request.AddPostRequest;
import one.whr.simple.dto.request.UpdatePostRequest;
import one.whr.simple.dto.response.MessageResponse;
import one.whr.simple.dto.response.PostResponse;
import one.whr.simple.entity.Category;
import one.whr.simple.entity.Post;
import one.whr.simple.entity.Tag;
import one.whr.simple.exceptions.CategoryNotFoundException;
import one.whr.simple.exceptions.PostNotFoundException;
import one.whr.simple.service.CategoryService;
import one.whr.simple.service.PostService;
import one.whr.simple.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/post")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@Slf4j
public class PostController {

    @Autowired
    PostService postService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    TagService tagService;


    @GetMapping("{postId}")
    ResponseEntity<?> getPost(@PathVariable Long postId) {
        log.info("called");
        Post post;
        try {
            post = postService.getPost(postId);
        } catch (PostNotFoundException e) {
            return ResponseEntity.ok().body(new MessageResponse(MessageCode.POST_NOT_FOUND, e.getMessage()));
        }
        return ResponseEntity.ok().body(new PostResponse(MessageCode.SUCCESSFUL, "Post found", post));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<?> addNewPost(@RequestBody AddPostRequest request) {
        try {
            Post newPost = new Post(request.getTitle(), request.getDescription(), request.getContent().getBytes(), request.getCoverUrl());
            newPost.setCreatedTime(LocalDateTime.now());
            Category category;
            if (request.getCategoryId() != null) {
                category = categoryService.findById(request.getCategoryId());
            } else {
                category = categoryService.findById(1L); // default is Uncategorized
            }
            newPost.setCategory(category);

            Set<Tag> tags;
            if (request.getTagIds() != null) {
                tags = new HashSet<>(tagService.findAllById(request.getTagIds()));
                newPost.setTags(tags);
            }

            postService.addNewPost(newPost);


        } catch (CategoryNotFoundException e) {
            return ResponseEntity.ok().body(new MessageResponse(MessageCode.CATEGORY_NOT_FOUND, "Cannot find category"));
        }

        return ResponseEntity.ok()
                .body(new MessageResponse(MessageCode.SUCCESSFUL, "Add new post success"));
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<?> updatePost(@RequestBody UpdatePostRequest request) {
        try {
            Post post = postService.getPost(request.getId());
            post.setTitle(request.getTitle());
            post.setDescription(request.getDescription());
            post.setContent(request.getContent().getBytes());
            post.setUpdatedTime(LocalDateTime.now());
            post.setCoverUrl(request.getCoverUrl());

            Category category = categoryService.findById(request.getCategoryId());
            post.setCategory(category);

            Set<Tag> tags;
            if (request.getTagIds() != null) {
                tags = new HashSet<>(tagService.findAllById(request.getTagIds()));
                post.setTags(tags);
            } else {
                post.setTags(null);
            }

            postService.updatePost(post);
            return ResponseEntity.ok().body(new MessageResponse(MessageCode.SUCCESSFUL, "Update post successfully"));

        } catch (PostNotFoundException e) {
            return ResponseEntity.ok().body(new MessageResponse(MessageCode.POST_NOT_FOUND, "Can't find post to update"));
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.ok().body(new MessageResponse(MessageCode.CATEGORY_NOT_FOUND, "Can't find category to update"));
        }
    }


}
