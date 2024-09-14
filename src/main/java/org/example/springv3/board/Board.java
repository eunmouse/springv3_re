package org.example.springv3.board;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.example.springv3.reply.Reply;
import org.example.springv3.user.User;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

// @Builder
// @AllArgsConstructor // 풀 생성자
@NoArgsConstructor // 빈 생성자 (하이버네이트가 om 할때 필요)
@Setter
@Getter
@Table(name = "board_tb")
@Entity // DB에서 조회하면 자동 매핑이됨
public class Board {
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_incremnt 설정, 시퀀스 설정
    @Id // PK 설정
    private Integer id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @CreationTimestamp
    private Timestamp createdAt;

    // fk
//    @JsonIgnore
    // Properties 는 내부안에 있는 애들을 지우는 것
//    @JsonIgnoreProperties({"password"}) // 자바의 메세지 컨버터가 이걸보고 오브젝트->JSON 변경 할 때, 이 안의 애들은 JSON 으로 변경안한다.
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

//    @JsonIgnore
//    @JsonIgnoreProperties({"board", "createdAt"}) // -> 이 친구는 건드리지마, 댓글 안에있는 board 와 createdAt 은 안건드리게 됨
    @OneToMany(mappedBy = "board")
    private List<Reply> replies;

    @Builder
    public Board(Integer id, String title, String content, Timestamp createdAt, User user) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Board{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", user=" + user +
                ", replies=" + replies +
                '}';
    }
}