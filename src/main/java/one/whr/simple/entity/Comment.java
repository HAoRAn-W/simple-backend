package one.whr.simple.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "COMMENT")
@Getter
@Setter
@NoArgsConstructor
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

//    @Column(name = "user_id")
//    private Long userId;

//    @Column(name = "reply_to")
//    private Long replyToId;  // comment id

    private String content;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "comment_date")
    private LocalDate commentDate;

    @Column(name = "like_cnt")
    private Integer likeCnt;

//    @ManyToOne
//    @JoinColumn(name = "post_id")
//    @JsonIgnore
//    private Post post;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL)
    private List<Comment> childComments;

    @ManyToOne
    @JoinColumn(name = "reply_to")
    @JsonIgnore
    private Comment parentComment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Comment(String content, LocalDate commentDate, int likeCnt) {
        this.content = content;
        this.commentDate = commentDate;
        this.likeCnt = likeCnt;
    }
}
