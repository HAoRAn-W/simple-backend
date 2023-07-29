package one.whr.simple.service;

import one.whr.simple.entity.Post;
import one.whr.simple.entity.Tag;
import one.whr.simple.exceptions.TagNotFoundException;
import one.whr.simple.repository.PostRepository;
import one.whr.simple.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class TagService {
    @Autowired
    TagRepository tagRepository;

    @Autowired
    PostRepository postRepository;


    public List<Tag> findAllById(Set<Long> tagIds) {
        return tagRepository.findAllById(tagIds);
    }

    public List<Tag> getTagList() {
        return tagRepository.findAll();
    }


    public void save(Tag tag) {
        tagRepository.save(tag);
    }

    public Tag findById(Long id) throws TagNotFoundException {
        return tagRepository.findById(id).orElseThrow(() -> new TagNotFoundException("Cannot find tag"));
    }

    public void removeTagById(Long tagId) throws TagNotFoundException {
        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new TagNotFoundException("Cannot find tag"));
        Set<Post> posts = tag.getPosts();
        for (Post post : posts) {
            post.getTags().remove((tag));
        }
        postRepository.saveAll(posts);
        tagRepository.deleteById(tagId);
    }
}
