package one.whr.simple.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
    private int type;

    private Long postId;

    private String content;

    private Long userId;

    private Long parentId;
}
