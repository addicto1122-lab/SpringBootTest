//package com.example.ch2;
//
//import com.querydsl.core.BooleanBuilder;
//import com.querydsl.core.Tuple;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import jakarta.persistence.EntityManager;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import java.util.List;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import static com.example.ch2.QBoard.board;
//
//@SpringBootTest
//@Transactional // 테스트 끝나면 DB 롤백시키겠다
//public class BoardQuerydslTest {
//    @Autowired
//    EntityManager em;
//    @Autowired
//    BoardRepository boardRepository;
//
//    JPAQueryFactory qf; //QueryDSL은 모든 쿼리의 시작점
//
//    @BeforeEach
//    public void setup(){
//        qf = new JPAQueryFactory(em);
//        for(int i = 1; i <= 100; i++){
//            Board list = new Board();
//            list.setTitle("title" + i);
//            list.setContent("content" + i);
//            list.setWriter("writer" + (i%5));
//            list.setViewCnt((long)i);
//            boardRepository.save(list);
//        }
//    }
//
//    @Test
//    @DisplayName("기본조회 - 작성자로 글찾기")
//    public void findByWriter(){
//        List<Board> boards = qf.selectFrom(board)
//                .where(board.writer.eq("writer1"))
//                .fetch();
//
//        assertEquals(20, boards.size());
//    }
//
//    @Test
//    @DisplayName("여러조건")
//    public void conditionTest(){
//        //select * from board whare title Like "title1%" and viewCnt >= 50
//        List<Board> boards = qf.selectFrom(board)
//                .where(board.title.like("title1%")
//                        .and(board.viewCnt.goe(50L)))
//                .fetch();
//
//        boards.forEach(board -> assertTrue(board.getViewCnt() >= 50));
//        boards.forEach(board -> assertTrue(board.getTitle().startsWith("title1")));
//    }
//
//    @Test
//    @DisplayName("writer별 게시글 수와 조회수 합계")
//    public void groupByTest() {
//
//        List<Tuple> result = qf
//                .select(
//                        board.writer,
//                        board.count(),
//                        board.viewCnt.sum()
//                )
//                .from(board)
//                .groupBy(board.writer)
//                .having(board.viewCnt.sum().goe(200L))
//                .orderBy(board.writer.asc())
//                .fetch();
//
//        result.forEach(tuple -> {
//            System.out.println("writer : " + tuple.get(board.writer));
//            System.out.println("게시글 수 : " + tuple.get(board.count()));
//            System.out.println("조회수 합계 : " + tuple.get(board.viewCnt.sum()));
//        });
//    }
//
//    @Test
//    @DisplayName("조건 조합형 동적 검색")
//    public void dynamicSearchTest() {
//
//        String writer = "writer1";
//        Long minViewCnt = 50L;
//        String titleKeyword = "title";
//
//        BooleanBuilder builder = new BooleanBuilder();
//
//        // writer가 입력됐으면
//        if (writer != null && !writer.isBlank()) {
//            builder.and(board.writer.eq(writer));
//        }
//
//        // 최소 조회수가 입력됐으면
//        if (minViewCnt != null) {
//            builder.and(board.viewCnt.goe(minViewCnt));
//        }
//
//        // 제목 키워드가 입력됐으면
//        if (titleKeyword != null && !titleKeyword.isBlank()) {
//            builder.and(board.title.contains(titleKeyword));
//            //lick() = "%titleKeyword%"
//        }
//
//        List<Board> boards = qf
//                .selectFrom(board)
//                .where(builder) // builder에 아무 조건도 담기지 않으면 where절 생략
//                .orderBy(board.viewCnt.desc())
//                .fetch();
//
//        boards.forEach(System.out::println);
//    }
//}
