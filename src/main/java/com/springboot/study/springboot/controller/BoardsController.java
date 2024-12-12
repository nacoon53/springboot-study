package com.springboot.study.springboot.controller;

import com.springboot.study.springboot.entity.Board;
import com.springboot.study.springboot.exception.BoardException;
import com.springboot.study.springboot.service.BoardViewService;
import com.springboot.study.springboot.service.BoardsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boards")  // 클래스 레벨에서 기본 경로를 설정
public class BoardsController {

    private final BoardsService boardsService;
    private final BoardViewService boardViewService;

    public BoardsController(BoardsService boardsService, BoardViewService boardViewService) {
        this.boardsService = boardsService;
        this.boardViewService = boardViewService;
    }

    // 게시글 생성 (Create)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createBoard(@RequestBody Board board) {
        try{
            Board createdBoard = boardsService.createBoard(board);  // BoardService에 위임
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBoard); // 성공 시
        } catch (BoardException e) {
            // 에러 메시지와 상태 코드 반환
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage())); // 에러 메시지를 JSON으로 변환
        }
    }

    // 게시판 목록 조회 (Read - 목록)
    @GetMapping
    public Page<Board> selectBoards(Pageable pageable) {
        return boardsService.getAllBoards(pageable);  // BoardService에 위임
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
    public ResponseEntity<?> updateBoard(@PathVariable Long id, @RequestBody Board boardDetails) {
        try{
            Board createdBoard = boardsService.updateBoard(id, boardDetails);
            return ResponseEntity.status(HttpStatus.OK).body(createdBoard); // 성공 시
        } catch (BoardException e) {
            // 에러 메시지와 상태 코드 반환
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage())); // 에러 메시지를 JSON으로 변환
        }
    }

    // 게시판 삭제 (Delete)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBoard(@PathVariable Long id) {
        boardsService.deleteBoard(id);  // BoardService에 위임
    }
}
