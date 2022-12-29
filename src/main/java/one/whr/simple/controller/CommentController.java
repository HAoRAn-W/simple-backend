package one.whr.simple.controller;

import one.whr.simple.dto.request.CommentRequest;
import one.whr.simple.dto.response.CommentListResponse;
import one.whr.simple.dto.response.MessageResponse;
import one.whr.simple.entity.Comment;
import one.whr.simple.entity.User;
import one.whr.simple.repository.CommentRepository;
import one.whr.simple.repository.PostRepository;
import one.whr.simple.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/comment")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CommentController {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @GetMapping("/all")
    public ResponseEntity<?> showAllComments(@RequestParam Long postId,
                                             @RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "2") int pageSize,
                                             @RequestParam(defaultValue = "commentDate-desc") String sort) {
        try {
            String[] sortArr = sort.split("-");
            String sortField = sortArr[0];
            Sort.Direction sortDirection = sortArr[1].equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            Sort.Order order = new Sort.Order(sortDirection, sortField);

            Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(order));

            List<Comment> commentList = commentRepository.findByPostId(postId, pageable);

            return ResponseEntity.ok().body(new CommentListResponse(commentList));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("load comment error", "load comment error"));

        }

    }

    @PostMapping("/add")
    public ResponseEntity<?> addComment(@RequestBody CommentRequest commentRequest) {
        Comment comment = new Comment(commentRequest.getContent(), LocalDate.now(), 0);
        try {
            User user = userRepository.findById(commentRequest.getUserId()).orElseThrow(() -> new RuntimeException("no user"));
            comment.setUser(user);

//            Post post = postRepository.findById(commentRequest.getPostId()).orElseThrow(() -> new RuntimeException("no post"));
            comment.setPostId(commentRequest.getPostId());

            Long replyToId = commentRequest.getReplyToId();
            if (replyToId != null) {
                Comment parent = commentRepository.findById(replyToId).orElseThrow(() -> new RuntimeException("no comment"));
                comment.setParentComment(parent);
            }

            commentRepository.save(comment);
            return ResponseEntity.ok().body(new MessageResponse("COMMENT_SUCCESS", "comment success"));

        } catch (RuntimeException e) {
            throw new RuntimeException(e);  //TODO
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteComment() {
        return null;
    }
}
