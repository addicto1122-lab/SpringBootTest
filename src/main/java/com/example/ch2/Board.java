package com.example.ch2;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Entity
@Getter
@Setter
public class Board {
    @Id
    // IDENTITY : DB에서 Auto Increment와 동일하게 자동 번호 증가
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;
    private String title;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String content;
    private Long viewCnt;
    @CreationTimestamp
    private Date inDate;
    @UpdateTimestamp
    private Date upDate;

    // .StackOverflowError
    // ★ @Data -> get,set,toString 자동생성
    // 양방향 상태에서 둘다 boards print시 StackOverflowError발생
    // (Boards.toString -> User.toString -> Boards의 board.toString -> User.toStirng 무한반복)

    @Override
    public String toString() {
        return "Board{" +
                "bno=" + bno +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", viewCnt=" + viewCnt +
                ", upDate=" + upDate +
                ", inDate=" + inDate +
                '}';
    }
}
