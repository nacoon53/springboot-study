package com.springboot.study.springboot.service;

import com.springboot.study.springboot.entity.Board;
import com.springboot.study.springboot.exception.BoardException;
import com.springboot.study.springboot.repository.BoardRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static reactor.core.publisher.Mono.when;

@ExtendWith(MockitoExtension.class)
class BoardsServiceTest {
    @InjectMocks
    private BoardsService boardsService;

    @Mock
    private BoardRepository boardRepository;

    @Test
    @DisplayName("게시글 생성에 성공했는지 확인")
    public void testCreateBoard_success() throws BoardException {
        //given
        Board board = Board.builder()
                .title("제목_테스트")
                .content("내용_테스트")
                .userId(99999L)
                .build();

        given(boardRepository.save(any(Board.class))).willAnswer(invocation -> {
            Board savedBoard = invocation.getArgument(0); // 전달된 Board 객체 가져오기
            savedBoard.setBoardId(11L); // boardId를 설정
            return savedBoard; // 설정된 객체 반환
        });

        //when
        Board createdBoard = boardsService.createBoard(board);

        //then
        assertThat(createdBoard.getBoardId())
                .isNotNull();
    }

    @Test
    @DisplayName("게시글 생성 시 컨텐츠 내용(필수값)이 없어서 실패했는지 확인")
    public void testCreateBoard_fail() throws BoardException {
        //given
        Board board = Board.builder()
                .title("제목_테스트")
                .content("")
                .userId(99999L)
                .build();

        //when
       Throwable thrown = catchThrowable(() -> boardsService.createBoard(board));

        //then
        assertThat(thrown).isInstanceOf(BoardException.class)
                .hasMessageContaining("내용이 비어있습니다.");
    }
}
