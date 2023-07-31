package one.whr.simple.entity.projection;

import one.whr.simple.entity.Category;

import java.time.LocalDateTime;

public interface PostProjection {
    Long getId();

    LocalDateTime getCreatedTime();

    LocalDateTime getUpdatedTime();

    String getTitle();

    String getDescription();

    Category getCategory();

    String getCoverUrl();

}
