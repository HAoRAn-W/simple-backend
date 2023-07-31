package one.whr.simple.dto.response;

import one.whr.simple.entity.Category;

import java.util.List;

public class CategoryListResponse extends MessageResponse {

    List<Category> categories;

    public CategoryListResponse(int code, String message, List<Category> categories) {
        super(code, message);
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
