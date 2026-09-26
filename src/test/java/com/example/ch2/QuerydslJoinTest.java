package com.example.ch2;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static com.example.ch2.QBoard.board;
import static com.example.ch2.QUser.user;

@SpringBootTest
@Transactional
public class QuerydslJoinTest {
    @Autowired
    EntityManager em;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BoardRepository boardRepository;

    JPAQueryFactory qf;

    @BeforeEach
    public void setUp(){
        qf = new JPAQueryFactory(em);
        // 작성자 1명 - 글 3개
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
    @DisplayName("연관관계 - 사용자의 이름으로 글 검색[user, board 테이블 조인]")
    // select b.* from board b join User u ON b.user_id = u.user_id where u.last_name = "Last"
    public void joinTest(){
        List<Board> list = qf.selectFrom(board)
                .join(board.user, user) // 연관관계이므로 바로 join가능
                .where(user.lastName.eq("Last"))
                .fetch();
        assertEquals(3, list.size());
    }


}
