package one.whr.simple.dto.response;

import one.whr.simple.entity.Post;
import one.whr.simple.entity.projection.PostInfo;

import java.util.List;

public class PostPageResponse extends MessageResponse{

    List<PostInfo> posts;
    int total;
    public PostPageResponse(int code, String message) {
        super(code, message);
    }

    public PostPageResponse(int code, String message, List<PostInfo> posts, int total) {
        super(code, message);
        this.posts = posts;
        this.total = total;
    }


    public List<PostInfo> getPosts() {
        return posts;
    }

    public void setPosts(List<PostInfo> posts) {
        this.posts = posts;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
