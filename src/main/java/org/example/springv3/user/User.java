package org.example.springv3.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Setter
@Getter
@Table(name = "user_tb")
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true, nullable = false)
    private String username; // 아이디
    @Column(nullable = false)
    private String password; // 해쉬 저장
    @Column(nullable = false)
    private String email;
    // 이메일 인증 여부
    // 19세 이상 여부

    private String profile;

    @CreationTimestamp
    private Timestamp createdAt;

    // 로그인 시도 5번 이상 틀렸는지!! 틀린 횟수도 들어감
    // 잠김 여부
    // 활성화 여부 (1년 뒤, 로그인 시도 했을 때 활성화 하시겠습니까 -> email Redirect 라던지 처리)
    // 탈퇴 여부 (row 를 삭제하는게 아니라, boolean 에 true/false 넣어서), 탈퇴 시간 (5년이 지나면 삭제)
    // 로그인 시에 device 장비 (window, max, 휴대폰, 컴퓨터)
    // ip 저장

    @Builder
    public User(Integer id, String username, String password, String email, Timestamp createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}