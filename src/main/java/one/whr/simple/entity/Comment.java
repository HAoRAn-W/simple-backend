package one.whr.simple.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "COMMENT")
@Getter
@Setter
@NoArgsConstructor
// TODO delete user, just use user id?
public class Comment {
    // TODO change to datetime
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    private String content;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "comment_date")
    private LocalDate commentDate;

    @Column(name = "like_cnt")
    private Integer likeCnt;

    @OneToMany(mappedBy = "parentComment")
    private List<Reply> replies;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Comment(String content, LocalDate commentDate, int likeCnt) {
        this.content = content;
        this.commentDate = commentDate;
        this.likeCnt = likeCnt;
    }
}
