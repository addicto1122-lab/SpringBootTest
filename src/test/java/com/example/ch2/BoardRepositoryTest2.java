//package com.example.ch2;
//
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@SpringBootTest
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//class BoardRepositoryTest2 {
//    @Autowired
//    private BoardRepository boardRepository;
//
//    @BeforeEach // 모든 테스트 실행 전에 먼저 실행
//    public void insertTest(){
//        for(int i=1; i<=100; i++){
//            {
//                Board board = new Board();
//                board.setTitle("Title"+i);
//                board.setWriter("writer" + (i%5));
//                board.setContent("content" + i);
//                board.setViewCnt(Math.round(Math.random()*100));
//                boardRepository.save(board);
//            }
//        }
//    }
//
//
//    @Test
//    void countAllByWriter() {
//        int cnt = boardRepository.countAllByWriter("writer1");
//        assertTrue(cnt == 20);
//
//    }
//
//    @Test
//    void findByTitleAndWriter() {
//        List<Board> boards = boardRepository.findByTitleAndWriter(
//                "Title1",
//                "writer0"
//        );
//        boards.forEach(System.out :: println);
//
//        assertTrue(boards.size() == 0);
//        // 검색 결과가 존재하지 않으면 빈 리스트 반환
////        assertNotNull(boards);
//    }
//
//    @Test
//    void deleteByTitle() {
//        assertTrue(boardRepository.deleteByTitle("Title1") == 1);
//        List<Board> boards = boardRepository.findByWriter("writer1");
//        boards.forEach(System.out :: println);
//    }
//
//    @Test
//    void findAllByOrderByViewCntDesc(){
//        List<Board> boards = boardRepository.findAllByOrderByViewCntDesc();
//        boards.forEach(System.out :: println);
//        // 정렬 검증 : 앞 게시글의 조회수가 뒤 게시물보다 크거나 같아야함
//        for(int i = 0; i < boards.size()-1; i ++){
//            assertTrue(boards.get(i).getViewCnt() >= boards.get(i+1).getViewCnt());
//        }
//    }
//
//    @Test
//    void findByWriterOrderByViewCntDesc(){
//        List<Board> boards = boardRepository.findByWriterOrderByViewCntDesc("writer1");
//        boards.forEach(System.out :: println);
//
//        for(int i = 0; i < boards.size()-1; i++){
//            assertTrue(boards.get(i).getViewCnt() >= boards.get(i+1).getViewCnt());
//        }
//    }
//}