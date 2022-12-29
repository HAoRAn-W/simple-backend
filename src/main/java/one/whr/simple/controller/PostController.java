package one.whr.simple.controller;

import one.whr.simple.dto.request.PostEditRequest;
import one.whr.simple.dto.response.MessageResponse;
import one.whr.simple.dto.response.PostDetailResponse;
import one.whr.simple.dto.response.PostListResponse;
import one.whr.simple.entity.Category;
import one.whr.simple.entity.Comment;
import one.whr.simple.entity.Post;
import one.whr.simple.entity.Tag;
import one.whr.simple.repository.CategoryRepository;
import one.whr.simple.repository.PostRepository;
import one.whr.simple.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

// TODO exception and error message, error logs
@RestController
@RequestMapping("/api/post")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PostController {

    @Autowired
    PostRepository postRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/all")
    public ResponseEntity<?> getAllPosts(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int pageSize,
                                         @RequestParam(defaultValue = "createdDate,asc") String[] sort) {
        try {

            String sortField = sort[0];
            Direction sortDirection = sort[1].equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            Order order = new Order(sortDirection, sortField);

            Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(order));

            Page<Post> postPage = postRepository.findAll(pageable);
            List<Post> posts = postPage.getContent();
            return ResponseEntity.ok().body(new PostListResponse(page, pageSize, posts));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("NO_CONTENT", "no content!"));

        }
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getPost(@RequestParam long postId) {
        try {
            Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("no post found"));
            List<Comment> comments = post.getComments();
            return ResponseEntity.ok().body(new PostDetailResponse(post, comments));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("NO_POST", "no post!"));
        }
    }

    @PutMapping("/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updatePost(@RequestBody PostEditRequest postEditRequest) {
        try {
            Post post = postRepository.findById(postEditRequest.getPostId()).orElseThrow(() -> new RuntimeException("post not found"));
            post.setContent(postEditRequest.getContent());

            List<Long> tagIds = postEditRequest.getTagIds();
            List<Tag> tags = tagRepository.findAllById(tagIds);
            post.setTags(tags);

            Category category = categoryRepository.findById(postEditRequest.getCategoryId()).orElseThrow(() -> new RuntimeException("post not found"));
            post.setCategory(category);

            post.setModifiedDate(LocalDate.now());

            postRepository.save(post);
            return ResponseEntity.ok(new MessageResponse("EDIT_SUCCESSFUL", "edit post successfully!"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("NO_POST", "no post!"));
        }
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deletePost(@RequestParam long postId) {
        try {
            postRepository.deleteById(postId);
            return ResponseEntity.ok(new MessageResponse("DELETE_SUCCESSFUL", "delete post successfully!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("NO_POST", "no post!"));
        }
    }

    // TODO redis like unlike function.
}
