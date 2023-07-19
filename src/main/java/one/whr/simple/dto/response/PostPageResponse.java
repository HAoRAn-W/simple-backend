package one.whr.simple.dto.response;

import one.whr.simple.entity.Post;

import java.util.List;

public class PostPageResponse extends MessageResponse{

    List<Post> posts;
    public PostPageResponse(int code, String message) {
        super(code, message);
    }

    public PostPageResponse(int code, String message, List<Post> posts) {
        super(code, message);
        this.posts = posts;
    }


    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
