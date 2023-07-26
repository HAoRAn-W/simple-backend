package one.whr.simple.entity.projection;

import one.whr.simple.entity.Category;
import one.whr.simple.entity.Tag;

import java.time.LocalDateTime;
import java.util.Set;

public interface PostProjection {
    Long getId();

    LocalDateTime getCreatedTime();

    LocalDateTime getUpdatedTime();

    String getTitle();

    String getDescription();

    Category getCategory();

    String getCoverUrl();

}
