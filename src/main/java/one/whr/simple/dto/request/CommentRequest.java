package one.whr.simple.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
    private Long postId;

    private String content;

    private Long userId;

    private Long replyToId;
}
