package one.whr.simple.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "COMMENT")
@Getter
@Setter
@NoArgsConstructor
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "reply_to")
    private Long replyToId;

    private String content;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "comment_date")
    private LocalDate commentDate;

    @Column(name = "like_cnt")
    private Integer likeCnt;
}
