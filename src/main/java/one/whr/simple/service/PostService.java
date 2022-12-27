package one.whr.simple.service;

import one.whr.simple.entity.Comment;
import one.whr.simple.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;


}
