//package com.example.ch2;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Page;
//import java.util.List;
//
////import static org.junit.jupiter.api.assertEquals;
//
//@SpringBootTest
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//class BoardRepositoryTest3 {
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
//    @Test
//    public void pagingTest(){
//        // PageRequest.of(페이지번호, 페이지당 갯수, 정렬 방향, 정렬 기준 필드)
//        // 페이지 번호 0부터 시작 ( 0 = 첫 페이지 )
//        Pageable pageable =
//                PageRequest.of(0, 10, Sort.Direction.DESC, "bno");
//        // 몇개씩?
//        Page<Board> page = boardRepository.findAll(pageable);
//        // 전체 글 갯수
//        System.out.println("전체 글 갯수: " + page.getTotalElements());
//        //전체 페이지 수
//        System.out.println("전체 페이지 갯수: " + page.getTotalPages());
//        //현재 페이지
//        System.out.println("현재 페이지: " + page.getNumber());
//        //이전 페이지
//        System.out.println("이전 페이지 여부: " + page.hasPrevious());
//        //다음 페이지
//        System.out.println("다음 페이지 여부: " + page.hasNext());
//
//        List<Board> boards = page.getContent(); //실제 게시판 글 꺼내기
//        boards.forEach(System.out :: println);
//
////        assertEquals
//    }
//}