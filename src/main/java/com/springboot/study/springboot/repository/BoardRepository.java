package com.springboot.study.springboot.repository;

import com.springboot.study.springboot.entity.Board;
import com.springboot.study.springboot.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByTitle(String title);

    @Modifying
    @Transactional
    @Query("UPDATE Board b SET b.viewCount = b.viewCount + :increment WHERE b.boardId = :boardId")
    void incrementViewCount(Long boardId, int increment);
}
