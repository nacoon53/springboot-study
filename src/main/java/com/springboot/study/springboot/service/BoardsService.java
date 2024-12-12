package com.springboot.study.springboot.service;

import com.springboot.study.springboot.entity.Board;
import com.springboot.study.springboot.exception.BoardException;
import com.springboot.study.springboot.repository.BoardRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
public class BoardsService {

    private final BoardRepository boardRepository;

    public BoardsService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    // 게시글 생성 (Create)
    public Board createBoard(Board board) throws BoardException {
        if(isBoardValid(board)) {
            return boardRepository.save(board);
        }

        throw new BoardException("게시글 생성에 실패하였습니다.");
    }

    // 게시판 수정 (Update)
    public Board updateBoard(Long id, Board boardDetails) throws BoardException{
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 수정된 값 적용
        board.setTitle(boardDetails.getTitle());
        board.setContent(boardDetails.getContent());
        board.setUserId(boardDetails.getUserId());

        if(isBoardValid(board)) {
            return boardRepository.save(board);
        }

        throw new BoardException("게시글 수정에 실패하였습니다.");
    }

    private boolean isBoardValid(Board board) throws BoardException{
        if(StringUtils.isEmpty(board.getTitle())) {
            throw new BoardException("제목이 비어있습니다.");
        }
        if(ObjectUtils.isEmpty(board.getContent())) {
            throw new BoardException("내용이 비어있습니다.");
        }
        if(ObjectUtils.isEmpty(board.getUserId())) {
            throw new BoardException("작성자 정보가 없습니다.");
        }

        return true;
    }

    // 게시판 목록 조회 (Read - 목록)
    public Page<Board> getAllBoards(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    // 게시판 단건 조회 (Read - 단건)
    public Board getBoardById(Long id) {
        Optional<Board> board = boardRepository.findById(id);
        return board.orElseThrow(() -> new RuntimeException("게시판을 찾을 수 없습니다."));
    }


    // 게시판 삭제 (Delete)
    public void deleteBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시판을 찾을 수 없습니다."));
        boardRepository.delete(board);
    }
}
