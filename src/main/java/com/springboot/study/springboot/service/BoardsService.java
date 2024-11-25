package com.springboot.study.springboot.service;

import com.springboot.study.springboot.entity.Board;
import com.springboot.study.springboot.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardsService {

    private final BoardRepository boardRepository;

    public BoardsService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    // 게시판 생성 (Create)
    public Board createBoard(Board board) {
        return boardRepository.save(board);
    }

    // 게시판 목록 조회 (Read - 목록)
    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    // 게시판 단건 조회 (Read - 단건)
    public Board getBoardById(Long id) {
        Optional<Board> board = boardRepository.findById(id);
        return board.orElseThrow(() -> new RuntimeException("게시판을 찾을 수 없습니다."));
    }

    // 게시판 수정 (Update)
    public Board updateBoard(Long id, Board boardDetails) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시판을 찾을 수 없습니다."));

        // 수정된 값 적용
        board.setTitle(boardDetails.getTitle());
        board.setContent(boardDetails.getContent());
        board.setUserId(boardDetails.getUserId());

        return boardRepository.save(board);
    }

    // 게시판 삭제 (Delete)
    public void deleteBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시판을 찾을 수 없습니다."));
        boardRepository.delete(board);
    }
}
