package one.whr.simple.dto.request;

public class UpdatePostRequest {

    private Long id;
    private String title;
    private String description;
    private String content;

    private Long categoryId;


    public UpdatePostRequest(Long id, String title, String description, String content, Long categoryId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.content = content;
        this.categoryId = categoryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
