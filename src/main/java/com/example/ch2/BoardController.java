package com.example.ch2;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    // 목록 : Get / board/list
    @GetMapping("/list")
    public void getList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "title") String type,
            @RequestParam(defaultValue = "") String keyword,
            Model model
    ){
        Page<Board> list =
                boardService.getList(page, type, keyword);

        model.addAttribute("list", list);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
    }

    // 읽기 : Get /board/read?bno=
    // 매개변수 이름과 테이블의 필드가 같으면 자동 바인딩 & 문자열 -> Long 자동형변환
    @GetMapping("/read")
    public void read(long bno, Model model){
        Board board = boardService.read(bno);
        model.addAttribute("board", board);
    }

    //작성: Get/board/write
    @GetMapping("/write")
    public void showWriteForm(Model model){
        Board board = new Board();
        User user = new User();
        user.setId(1L); // 로그인 기능구현 X 임시 데이터
        board.setUser(user);
        model.addAttribute("board", board);
    }

    //작성 처리: post /board/write
    @PostMapping("/write")
    public String write(Board board){
        board.setViewCnt(0L); // 조회수 초기화
        boardService.write(board);
        return "redirect:/board/list";
    }

    @PostMapping("/remove")
    public String remove(long bno){
        boardService.remove(bno);
        return "redirect:/board/list";
    }

    @PostMapping("/modify")
    public String modify(Board board){
        boardService.modify(board);
        return "redirect:/board/list";
    }

    @GetMapping("/writer")
    public String writer(
            long userId,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ){
        Page<Board> list = boardService.getListByUser(userId, page);

        model.addAttribute("list", list);
        model.addAttribute("userId", userId);

        return "board/writer";
    }

    // 작성자별 게시글 통계
    @GetMapping("/stats")
    public void stats(Model model){
        List<WriterStats> stats = boardService.getWriterStats();
        model.addAttribute("stats", stats);
    }
}
