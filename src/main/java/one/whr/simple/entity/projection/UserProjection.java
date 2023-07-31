package one.whr.simple.entity.projection;

import java.util.Set;

public interface UserProjection {
    String getUsername();

    Set<PostProjection> getFavorites();

}
