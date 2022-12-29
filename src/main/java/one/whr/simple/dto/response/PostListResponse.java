package one.whr.simple.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import one.whr.simple.entity.Post;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostListResponse {
    private int page;

    private int pageSize;

    private List<Post> posts;

}
