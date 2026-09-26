package com.example.ch2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class OneToOneTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CartRepository cartRepository;

    @Test
    public void saveTest(){
        Member member = new Member();
        member.setId("aaa");
        member.setName("에이");
        member.setEmail("aaa@aaa.com");
        member.setPassword("1234");
        memberRepository.save(member);

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setMember(member); // 객체를 통째로 넣으면 DB에는 member_id만 저장됨
        cartRepository.save(cart);
    }

    @Test
    @DisplayName("Member정보에서 Cart정보 꺼내기")
    public void findCartTest(){
        saveTest();
        Member member = memberRepository.findById("aaa").orElse(null);
        assertNotNull(member);
        System.out.println(member.getCart().getId());
    }

    @Test
    @DisplayName("제약 위반")
    public void NullMemberTest(){
        Cart cart = new Cart();
        cart.setId(2L);
        assertThrows(Exception.class, () -> cartRepository.save(cart));
    }
}
