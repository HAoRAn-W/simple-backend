package one.whr.simple.dto.response;

import one.whr.simple.entity.projection.PostProjection;

import java.util.List;

public class PostPageResponse extends MessageResponse{

    List<PostProjection> posts;
    int total;
    public PostPageResponse(int code, String message) {
        super(code, message);
    }

    public PostPageResponse(int code, String message, List<PostProjection> posts, int total) {
        super(code, message);
        this.posts = posts;
        this.total = total;
    }


    public List<PostProjection> getPosts() {
        return posts;
    }

    public void setPosts(List<PostProjection> posts) {
        this.posts = posts;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
