package one.whr.simple.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import one.whr.simple.entity.Post;

@Getter
@Setter
@AllArgsConstructor
public class PostDetailResponse {
    private Post post;

//    private List<Comment> comments;
}
