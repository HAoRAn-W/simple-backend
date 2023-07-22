package one.whr.simple.dto.response;

import one.whr.simple.entity.projection.PostProjection;

import java.util.Set;

public class FavoriteListResponse extends MessageResponse{
    Set<PostProjection> favorites;
    public FavoriteListResponse(int code, String message, Set<PostProjection> favorites) {
        super(code, message);
        this.favorites = favorites;
    }

    public Set<PostProjection> getFavorites() {
        return favorites;
    }

    public void setFavorites(Set<PostProjection> favorites) {
        this.favorites = favorites;
    }
}
