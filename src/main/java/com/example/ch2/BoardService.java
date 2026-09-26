package com.example.ch2;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private BoardRepository repository;

    // 전체 게시물 조회
    public List<Board> getList(){
        return boardRepository.findAll();
    }

    // 게시물 저장
    public Board write(Board board){
        return boardRepository.save(board);
    }

    // 게시물 상세보기 (없으면 null반환)
    public Board read(Long bno){
        return boardRepository.findById(bno).orElse(null);
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
}
