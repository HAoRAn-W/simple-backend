package one.whr.simple.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import one.whr.simple.entity.Comment;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CommentListResponse {
    List<Comment> commentList;
}
