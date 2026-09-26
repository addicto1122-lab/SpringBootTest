//package com.example.ch2;
//
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//class BoardRepositoryTest {
//    @Autowired
//    private BoardRepository boardRepository;
//
//    @Test
//    @Order(1) // 메서드 실행 순서 정하기
//    public void insertTest()
//    {
//        Board board = new Board();
//        board.setTitle("Test Title");
//        board.setWriter("aaa");
//        board.setContent("Test Content");
//        boardRepository.save(board);
//    }
//
//    @Test
//    @Order(2)
//    void findByWriter() {
//        List<Board> boards = boardRepository.findByWriter("aaa");
//        assertEquals(boards.size(), 1);
//    }
//
//    @Test
//    void countAllByWriter() {
//
//    }
//
//    @Test
//    void findByTitleAndWriter() {
//
//    }
//
//    @Test
//    void deleteByTitle() {
//    }
//}