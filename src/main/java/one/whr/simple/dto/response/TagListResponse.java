package one.whr.simple.dto.response;

import one.whr.simple.entity.Tag;

import java.util.List;

public class TagListResponse extends MessageResponse {

    List<Tag> tags;

    public TagListResponse(int code, String message, List<Tag> tags) {
        super(code, message);
        this.tags = tags;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
