package one.whr.simple.dto.response.entity;

import one.whr.simple.entity.Category;
import one.whr.simple.entity.Post;
import one.whr.simple.entity.Tag;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Set;

// String type content
public class StrPost {
    private Long id;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private String title;

    private String description;

    private String content;

    private Category category;

    private String coverUrl;

    private Set<Tag> tags;

    private Boolean pinned;

    public StrPost() {
    }

    public StrPost(Post post) {
        this.id = post.getId();
        this.createdTime = post.getCreatedTime();
        this.updatedTime = post.getUpdatedTime();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.content = new String(post.getContent(), StandardCharsets.UTF_8);
        this.category = post.getCategory();
        this.coverUrl = post.getCoverUrl();
        this.tags = post.getTags();
        this.pinned = post.getPinned();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Boolean getPinned() {
        return pinned;
    }

    public void setPinned(Boolean pinned) {
        this.pinned = pinned;
    }
}
