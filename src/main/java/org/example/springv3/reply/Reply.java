package org.example.springv3.reply;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.springv3.board.Board;
import org.example.springv3.user.User;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@NoArgsConstructor
@Setter
@Getter
@Table(name = "reply_tb")
@Entity // DB에서 조회하면 자동 매핑이됨
public class Reply {
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment 설정, 시퀀스 설정
    @Id // PK 설정
    private Integer id;

    private String comment; // 댓글 내용

    @JsonIgnoreProperties({"password"}) // 자바의 메세지 컨버터가 이걸보고 오브젝트->JSON 변경 할 때, 이 안의 애들은 JSON 으로 변경안한다.
    @ManyToOne(fetch = FetchType.LAZY)
    private User user; // foreign key 대신에 오브젝트 넣어줌 (ORM)

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board; // foreign key 대신에 오브젝트 넣어줌 (ORM)

    @CreationTimestamp // em.persist 할때 발동 (NativeQuery 쓸 때는 발동안함)
    private Timestamp createdAt;

    @Builder
    public Reply(Integer id, String comment, User user, Board board, Timestamp createdAt) {
        this.id = id;
        this.comment = comment;
        this.user = user;
        this.board = board;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Reply{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                ", user=" + user +
                ", createdAt=" + createdAt +
                '}';
    }
}