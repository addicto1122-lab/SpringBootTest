package com.example.ch2;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ManyToOne {
    @Autowired
    UserRepository userRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    EntityManager entityManager;

    @BeforeEach
    public void setUp(){
        User user = new User();
        user.setFirstName("First");
        user.setLastName("Last");
        userRepository.save(user);

        for(int i = 1; i <= 3; i++){
            Board board = new Board();
            board.setTitle("title" + i);
            board.setUser(user);
            board.setContent("content"+ i);
            board.setViewCnt((long)i);
            boardRepository.save(board);
        }
    }

    @Test
    @DisplayName("특정 글의 작성자 확인")
    public void boardToUserTest(){
        Board board = boardRepository.findById(1L).orElse(null);
        assertNotNull(board);

        System.out.println("작성자: " + board.getUser().getFirstName());
        assertEquals("First", board.getUser().getFirstName());
    }

    @Test
    // 트랜젝션 어노테이션 사용X -> LazyInitializationException
    // @Transactional로 테스트 진행시 DB를 다시 조회하지 않음. persistentContext만 확인
    @Transactional
    @DisplayName("작성자가 쓴 글 목록 확인(1 -> N)")
    public void userToBoardTest(){
        entityManager.flush(); // board에 추가한 insert를 DB에 반영
        entityManager.clear(); // 캐시 비우기
        User user = userRepository.findById(1L).orElse(null);
        assertNotNull(user);

        List<Board> boars = user.getBoards();
        boars.forEach(System.out :: println);
        assertEquals(3, boars.size());
    }

}
