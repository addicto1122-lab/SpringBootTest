package com.example.ch2;

import com.google.errorprone.annotations.IncompatibleModifiers;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity // JPA 사용시 객체 등록하는 어노테이션
@Data
public class User {
//    @Id // pk지정
//    private String id;
//    private String pwd;
//    private String name;
//    private String email;
//    private String birth;
//    private String sns;
//    private Date reg_date;

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;
    private String firstName;
    private String lastName;

    // fetch 전략
    // - EAGER : User 조회시 게시글도 모두 찾음 (boards에 작성글 모두 지정)
    // - LAZY(기본) : User만 조회 -> 게시글은 찾지 않음(boards에 없음)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Board> boards = new ArrayList<>();
}
