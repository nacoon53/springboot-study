package com.springboot.study.springboot.repository;

import com.springboot.study.springboot.entity.Board;
import com.springboot.study.springboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByTitle(String title);

}
