package one.whr.simple.controller;

import one.whr.simple.dto.Response.MessageResponse;
import one.whr.simple.dto.Response.PostDetailResponse;
import one.whr.simple.dto.Response.PostListResponse;
import one.whr.simple.entity.Comment;
import one.whr.simple.entity.Post;
import one.whr.simple.repository.PostRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PostController {
    @Value("${whr.app.config.page-size}")
    int pageSize;
    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

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
            return  ResponseEntity.badRequest().body(new MessageResponse("NO_CONTENT", "no content!"));

        }
    }

    @GetMapping("/post")
    public ResponseEntity<?> getPost(@RequestParam long postId) {
        try {
            Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("no post found"));
            List<Comment> comments = post.getComments();
            return ResponseEntity.ok().body(new PostDetailResponse(post, comments));
        } catch (RuntimeException e) {
            return  ResponseEntity.badRequest().body(new MessageResponse("NO_POST", "no post!"));

        }
    }


}
