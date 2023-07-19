package one.whr.simple.service;

import one.whr.simple.entity.Post;
import one.whr.simple.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;

    public Page<Post> getPaginatedPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size,Sort.by("createdTime").descending());
        return postRepository.findAll(pageable);
    }

    public void addNewPost(Post post) {
        postRepository.save(post);
    }
}
