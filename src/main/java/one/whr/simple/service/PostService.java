package one.whr.simple.service;

import one.whr.simple.entity.Post;
import one.whr.simple.entity.projection.PostInfo;
import one.whr.simple.exceptions.PostNotFoundException;
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

    public Page<PostInfo> getPaginatedPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size,Sort.by("createdTime").descending());
        return postRepository.findAllBy(pageable);
    }

    public void addNewPost(Post post) {
        postRepository.save(post);
    }

    public Post getPost(Long id) throws PostNotFoundException {
        return postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Can't find post with this id"));
    }

    public void updatePost(Post post) {
        postRepository.save(post);
    }
}
