package one.whr.simple.service;

import one.whr.simple.entity.Category;
import one.whr.simple.entity.Post;
import one.whr.simple.entity.projection.PostProjection;
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

    public Page<PostProjection> getPaginatedPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size,Sort.by("createdTime").descending());
        return postRepository.findAllBy(pageable);
    }

    public Page<PostProjection> getPaginatedPostsByCategory(int page, int size, Category category) {
        Pageable pageable = PageRequest.of(page, size,Sort.by("createdTime").descending());
        return postRepository.findAllByCategory(category, pageable);
    }

    public Page<PostProjection> getPaginatedPostsByTagId(int page, int size, Long tagId) {
        Pageable pageable = PageRequest.of(page, size,Sort.by("createdTime").descending());
        return postRepository.findAllByTagsId(tagId, pageable);
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
