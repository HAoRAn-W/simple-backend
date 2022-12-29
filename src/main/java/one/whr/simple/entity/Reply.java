package one.whr.simple.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "REPLY")
@Getter
@Setter
@NoArgsConstructor
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String content;

    @Column(name = "comment_date")
    private LocalDate commentDate;

    @Column(name = "like_cnt")
    private int likeCnt;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Comment parentComment;

    public Reply(String content, LocalDate commentDate, int likeCnt) {
        this.content = content;
        this.commentDate = commentDate;
        this.likeCnt = likeCnt;
    }
}
