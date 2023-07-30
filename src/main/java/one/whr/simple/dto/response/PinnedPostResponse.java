package one.whr.simple.dto.response;

import one.whr.simple.entity.Post;
import one.whr.simple.entity.projection.PostProjection;

import java.util.List;

public class PinnedPostResponse extends MessageResponse{

    List<PostProjection> posts;
    public PinnedPostResponse(int code, String message,List<PostProjection> posts) {
        super(code, message);
        this.posts = posts;
    }

    // remeber to create getter otherwise cannot generate necessary field when serialize the object to form JSON
    public List<PostProjection> getPosts() {
        return posts;
    }

    public void setPosts(List<PostProjection> posts) {
        this.posts = posts;
    }
}
