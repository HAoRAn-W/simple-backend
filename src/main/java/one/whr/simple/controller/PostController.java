package one.whr.simple.controller;

import lombok.extern.slf4j.Slf4j;
import one.whr.simple.constant.MessageCode;
import one.whr.simple.dto.request.AddPostRequest;
import one.whr.simple.dto.request.UpdatePostRequest;
import one.whr.simple.dto.response.MessageResponse;
import one.whr.simple.dto.response.PinnedPostResponse;
import one.whr.simple.dto.response.PostResponse;
import one.whr.simple.entity.Category;
import one.whr.simple.entity.Post;
import one.whr.simple.entity.Tag;
import one.whr.simple.entity.projection.PostProjection;
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
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/post")
@Slf4j
public class PostController {

    PostService postService;

    CategoryService categoryService;

    TagService tagService;

    @Autowired
    public PostController(PostService postService, CategoryService categoryService, TagService tagService) {
        this.postService = postService;
        this.categoryService = categoryService;
        this.tagService = tagService;
    }

    @GetMapping("/{postId}")
    ResponseEntity<?> getPost(@PathVariable Long postId) throws PostNotFoundException {
        log.info("called");
        Post post;
        post = postService.getPost(postId);
        return ResponseEntity.ok().body(new PostResponse(MessageCode.SUCCESSFUL, "Post found", post));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<?> addNewPost(@RequestBody AddPostRequest request) throws CategoryNotFoundException {
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

        return ResponseEntity.ok()
                .body(new MessageResponse(MessageCode.SUCCESSFUL, "Add new post success"));
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<?> updatePost(@RequestBody UpdatePostRequest request) throws PostNotFoundException, CategoryNotFoundException {
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

        post.setPinned(request.getPinned());

        postService.updatePost(post);
        return ResponseEntity.ok().body(new MessageResponse(MessageCode.SUCCESSFUL, "Update post successfully"));
    }

    @GetMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<?> removePost(@RequestParam Long postId) throws PostNotFoundException {
        postService.removePost(postId);
        return ResponseEntity.ok().body(new MessageResponse(MessageCode.SUCCESSFUL, "Delete post successfully"));
    }

    @GetMapping("/pin/{postId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<?> pinPost(@PathVariable Long postId) throws PostNotFoundException {
        Post post = postService.getPost(postId);
        post.setPinned(true);
        postService.updatePost(post);
        return ResponseEntity.ok().body(new MessageResponse(MessageCode.SUCCESSFUL, "pin post successfully"));
    }

    @GetMapping("/unpin/{postId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<?> unpinPost(@PathVariable Long postId) throws PostNotFoundException {
        Post post = postService.getPost(postId);
        post.setPinned(false);
        postService.updatePost(post);
        return ResponseEntity.ok().body(new MessageResponse(MessageCode.SUCCESSFUL, "unpin post successfully"));
    }

    @GetMapping("/pin/all")
    ResponseEntity<?> getPinnedPosts() {
        List<PostProjection> posts = postService.getPinnedPosts();
        return ResponseEntity.ok().body(new PinnedPostResponse(MessageCode.SUCCESSFUL, "get pinned list successful", posts));
    }
}
