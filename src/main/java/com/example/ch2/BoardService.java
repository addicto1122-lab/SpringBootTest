package com.example.ch2;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    // 게시물 조회
    public Page<Board> getList(int page, String type, String keyword){

        Pageable pageable = PageRequest.of(
                page,
                10,
                Sort.by("bno").descending()
        );

        // 검색어가 없으면 전체 목록
        if(keyword == null || keyword.isBlank()){
            return boardRepository.findAll(pageable);
        }

        if(type.equals("title")){
            return boardRepository.findByTitleContaining(
                    keyword,
                    pageable
            );
        }

        if(type.equals("content")){
            return boardRepository.findByContentContaining(
                    keyword,
                    pageable
            );
        }

        // 제목 + 내용
        return boardRepository
                .findByTitleContainingOrContentContaining(
                        keyword,
                        keyword,
                        pageable
                );
    }

    // 게시물 저장
    public Board write(Board board){
        return boardRepository.save(board);
    }

    // 게시물 상세보기 (없으면 null반환)
    public Board read(Long bno){
        Board board =  boardRepository.findById(bno).orElse(null);

        if(board == null){
            return null;
        }
        board.setViewCnt(board.getViewCnt() + 1);
        return boardRepository.save(board);
    }

    // 게시물 수정
    // 클라이언트가 요청한 newBoard그대로 save하면 안됌(조회수, inDate 초기화때문)
    // 게시글 수정 폼에는 title/content만 있기 때문에 나머지 board의 필드는 초기화 or null이 된다
    // -> DB에서 원본 게시글 읽기 -> 바뀐 필드만 수정하기
    public Board modify(Board newBoard){
        Board board = boardRepository.findById(newBoard.getBno()).orElse(null);
        if(board == null){
            return null;
        }
        board.setContent(newBoard.getContent());
        board.setTitle(newBoard.getTitle());
        return boardRepository.save(board);
    }

    // 게시물 삭제 (게시글 존재 확인 후)
    public void remove(Long bno){
        Board board = boardRepository.findById(bno).orElse(null);
        if(board != null) {
            boardRepository.deleteById(bno);
        }
    }

    public Page<Board> getListByUser(Long userId, int page){
        Pageable pageable = PageRequest.of(
                page,
                10,
                Sort.by("bno").descending()
        );
        return boardRepository.findByUser_Id(
                userId,
                pageable
        );
    }

    // 작성자별 통계
    public List<WriterStats> getWriterStats(){
        return boardRepository.findWriterStats();
    }
}
