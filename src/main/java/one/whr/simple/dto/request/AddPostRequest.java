package one.whr.simple.dto.request;

public class AddPostRequest {
    private String title;
    private String description;
    private String content;

    private Long categoryId;

    public AddPostRequest() {}

    public AddPostRequest(String title, String description, String content, Long categoryId) {
        this.title = title;
        this.description = description;
        this.content = content;
        this.categoryId = categoryId;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
