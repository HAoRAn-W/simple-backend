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
@Table(name = "POST")
@Getter
@Setter
@NoArgsConstructor
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "date_created")
    private LocalDate createdDate;

    @Column(name = "date_modified")
    private LocalDate modifiedDate;

    @Column(name = "view_cnt")
    private Integer viewCnt;

    @Column(name = "like_cnt")
    private Integer likeCnt;

    private String content;

    @Column(name = "cate_id")
    private Long categoryId;
}
