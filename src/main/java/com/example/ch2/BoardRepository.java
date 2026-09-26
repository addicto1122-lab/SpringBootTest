package com.example.ch2;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
//public interface BoardRepository extends CrudRepository<Board, Long> {
public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findByTitleContaining(
            String Keyword,
            Pageable pageable
    );

    Page<Board> findByContentContaining(
            String keyword,
            Pageable pageable
    );

    Page<Board> findByTitleContainingOrContentContaining(
            String title,
            String content,
            Pageable pageable
    );

    Page<Board> findByUser_Id(Long userId, Pageable pageable);


    @Query("""
        SELECT b.user.id AS userId,
               COUNT(b) AS postCount,
               SUM(b.viewCnt) AS totalViews,
               AVG(b.viewCnt) AS averageViews
        FROM Board b
        GROUP BY b.user.id
        ORDER BY b.user.id
        """)
    List<WriterStats> findWriterStats();
    //    //JPA는 메서드 이름에 특정 규칙 적용.
//    //메서드 이름에 따라 자동으로 쿼리 작성 가능
//
//    // SELECT * FROM board WHERE writer=?
//    List<Board> findByWriter(String writer);
//
//    //SELECT COUNT() FROM board WHERE writer=?
//    int countAllByWriter(String writer);
//
//    //SELECT FROM board WHERE title=? AND writer=?
//    List<Board> findByTitleAndWriter(String title, String writer);
//
//    //DELETE FROM board WHERE title=?
//    // 여러 행이 동시에 삭제 될 가능성이 있다 -> 트랜잭션 처리 필수
//    @Transactional
//    int deleteByTitle(String title);
//
//    // 전체 게시물을 조회수로 내림차순
//    // select * from Board ORDER BY viewCnt DESC
//    // findAllBy + OrderBy + 필드명 + Desc | Asc
//    List<Board> findAllByOrderByViewCntDesc();
//
//    // select * from Board where Writer = : writer ORDER BY viewCnt Desc
//    List<Board> findByWriterOrderByViewCntDesc(String writer);
}