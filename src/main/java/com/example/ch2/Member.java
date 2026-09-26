package com.example.ch2;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "member")
@Data
public class Member {

    @Id
    @Column(name = "member_id")
    private String id;

    private String password;
    private String name;
    private String email;

    //양방향 관계 설정
    //Member 정보만으로 Cart 정보를 찾기 위해
    //mappedMy = "member" : FK관리는 Cart에서 하겠다 -> Member엔 FK생성되지 않음.
    @OneToOne(mappedBy = "member")
    private Cart cart;
}