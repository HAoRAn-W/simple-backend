package one.whr.simple.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class PostEditRequest {
    private Long postId;

    private String content;

    private List<Long> tagIds;

    private Long categoryId;
}
