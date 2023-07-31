package one.whr.simple.service;

import one.whr.simple.entity.Category;
import one.whr.simple.entity.Post;
import one.whr.simple.entity.Tag;
import one.whr.simple.entity.User;
import one.whr.simple.entity.projection.PostProjection;
import one.whr.simple.exceptions.PostNotFoundException;
import one.whr.simple.repository.PostRepository;
import one.whr.simple.repository.TagRepository;
import one.whr.simple.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    UserRepository userRepository;

    public Page<PostProjection> getPaginatedPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdTime").descending());
        return postRepository.findAllBy(pageable);
    }

    public Page<PostProjection> getPaginatedPostsByCategory(int page, int size, Category category) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdTime").descending());
        return postRepository.findAllByCategory(category, pageable);
    }

    public Page<PostProjection> getPaginatedPostsByTagId(int page, int size, Long tagId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdTime").descending());
        return postRepository.findAllByTagsId(tagId, pageable);
    }

    public Page<PostProjection> getPaginatedPostsByUserFavorite(String username, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return postRepository.findAllByFavoriteUsersUsername(username, pageable);
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

    public void removePost(Long postId) throws PostNotFoundException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Can't find post with this id"));
        Set<Tag> tags = post.getTags();
        for (Tag tag : tags) {
            tag.getPosts().remove(post);
        }
        tagRepository.saveAll(tags);
        Set<User> users = post.getFavoriteUsers();
        for (User user : users) {
            user.getFavorites().remove(post);
        }
        userRepository.saveAll(users);
        postRepository.deleteById(postId);
    }


    public List<PostProjection> getPinnedPosts() {
        return postRepository.findAllByPinned(true);
    }
}
