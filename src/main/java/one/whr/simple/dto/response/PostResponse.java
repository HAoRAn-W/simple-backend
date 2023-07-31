package one.whr.simple.dto.response;

import one.whr.simple.dto.response.entity.StrPost;
import one.whr.simple.entity.Post;

public class PostResponse extends MessageResponse {

    StrPost post;

    public PostResponse(int code, String message, Post post) {
        super(code, message);
        this.post = new StrPost(post);
    }

    public StrPost getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = new StrPost((post));
    }
}
