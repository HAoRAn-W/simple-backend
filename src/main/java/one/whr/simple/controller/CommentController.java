package one.whr.simple.controller;

import one.whr.simple.dto.request.CommentRequest;
import one.whr.simple.dto.response.MessageResponse;
import one.whr.simple.entity.Comment;
import one.whr.simple.entity.Post;
import one.whr.simple.entity.User;
import one.whr.simple.repository.CommentRepository;
import one.whr.simple.repository.PostRepository;
import one.whr.simple.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

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

    @PostMapping("/add")
    public ResponseEntity<?> addComment(@RequestBody CommentRequest commentRequest) {
        Comment comment = new Comment(commentRequest.getContent(), LocalDate.now());
        try {
            User user = userRepository.findById(commentRequest.getUserId()).orElseThrow(()->new RuntimeException("no user"));
            comment.setUser(user);

            Post post = postRepository.findById(commentRequest.getPostId()).orElseThrow(()->new RuntimeException("no post"));
            comment.setPost(post);

            Long replyToId = commentRequest.getReplyToId();
            if (replyToId != null) {
                Comment parent = commentRepository.findById(replyToId).orElseThrow(()->new RuntimeException("no comment"));
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
