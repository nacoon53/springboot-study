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
import org.springframework.data.domain.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

        //then
        assertThatThrownBy(() -> boardsService.createBoard(board))
                .isInstanceOf(BoardException.class)
                .hasMessage("내용이 비어있습니다.");
    }

    @Test
    @DisplayName("게시글 수정에 성공했는지 확인")
    public void testUpdateBoard_success() throws BoardException {
        //given
        Board createdBoard = Board.builder()
                .boardId(0L)
                .title("제목_테스트")
                .content("내용_테스트")
                .userId(99999L)
                .build();

        Board willUpdateBoard = Board.builder()
                .title("제목_테스트(수정)")
                .content("내용_테스트")
                .build();

        given(boardRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(createdBoard));

        given(boardRepository.save(any(Board.class))).willAnswer(invocation -> {
            Board updateBoard = invocation.getArgument(0); // 전달된 Board 객체 가져오기
            createdBoard.setTitle(updateBoard.getTitle());
            createdBoard.setContent(updateBoard.getContent());

            return createdBoard; // 설정된 객체 반환
        });

        //when
        Board updatedBoard = boardsService.updateBoard(0L, willUpdateBoard);

        //then
        assertThat(updatedBoard.getBoardId())
                .isEqualTo(0L);
        assertThat(updatedBoard.getTitle())
                .isEqualTo("제목_테스트(수정)");
    }

    @Test
    @DisplayName("게시글 수정 시 컨텐츠 내용(필수값)이 없어서 실패했는지 확인")
    public void testUpdateBoard_fail() throws BoardException {
        //given
        Board createdBoard = Board.builder()
                .boardId(0L)
                .title("제목_테스트")
                .content("내용_테스트")
                .userId(99999L)
                .build();

        Board willUpdateBoard = Board.builder()
                .title("제목_테스트(수정)")
                .content("")
                .build();

        given(boardRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(createdBoard));

        //then
        assertThatThrownBy(() -> boardsService.updateBoard(0L, willUpdateBoard))
                .isInstanceOf(BoardException.class)
                .hasMessage("내용이 비어있습니다.");
    }

    @Test
    @DisplayName("저장된 전체 게시물을 가져오는 지 확인")
    public void testGetAllBoards_success() {
        //given
        // 페이징 요청 정보 생성
        Pageable pageable = PageRequest.of(0, 2, Sort.by("title").ascending());

        // Mock 데이터 생성
        Board board = Board.builder()
                .title("제목_테스트")
                .content("내용_테스트")
                .userId(99999L)
                .build();

        List<Board> boardList = Arrays.asList(
                Board.builder()
                        .title("제목_테스트")
                        .content("내용_테스트")
                        .userId(99999L)
                        .build(),
                Board.builder()
                        .title("제목_테스트2")
                        .content("내용_테스트2")
                        .userId(88888L)
                        .build()
        );
        Page<Board> mockPage = new PageImpl<>(boardList, pageable, boardList.size());

        // Mock 설정
        given(boardRepository.findAll(any(Pageable.class))).willReturn(mockPage);

        //when
        Page<Board> result = boardsService.getAllBoards(pageable);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("제목_테스트");
        assertThat(result.getContent().get(1).getTitle()).isEqualTo("제목_테스트2");

        // Repository 메서드가 올바르게 호출되었는지 확인
        verify(boardRepository, times(1)).findAll(pageable);

    }
}
