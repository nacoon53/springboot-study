package com.springboot.study.springboot.controller;

import com.springboot.study.springboot.entity.Board;
import com.springboot.study.springboot.service.BoardViewService;
import com.springboot.study.springboot.service.BoardsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")  // 클래스 레벨에서 기본 경로를 설정
public class BoardsController {

    private final BoardsService boardsService;
    private final BoardViewService boardViewService;

    public BoardsController(BoardsService boardsService, BoardViewService boardViewService) {
        this.boardsService = boardsService;
        this.boardViewService = boardViewService;
    }

    // 게시판 생성 (Create)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Board createBoard(@RequestBody Board board) {
        return boardsService.createBoard(board);  // BoardService에 위임
    }

    // 게시판 목록 조회 (Read - 목록)
    @GetMapping
    public List<Board> selectBoards() {
        return boardsService.getAllBoards();  // BoardService에 위임
    }

    // 게시판 단건 조회 (Read - 단건)
    @GetMapping("/{id}")
    public Board selectBoard(@PathVariable Long id) {
        //조회수 증가
        boardViewService.incrementViewCount(id);

        return boardsService.getBoardById(id);  // BoardService에 위임
    }

    // 게시판 수정 (Update)
    @PutMapping("/{id}")
    public Board updateBoard(@PathVariable Long id, @RequestBody Board boardDetails) {
        return boardsService.updateBoard(id, boardDetails);  // BoardService에 위임
    }

    // 게시판 삭제 (Delete)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBoard(@PathVariable Long id) {
        boardsService.deleteBoard(id);  // BoardService에 위임
    }
}
