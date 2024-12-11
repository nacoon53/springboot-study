package com.springboot.study.springboot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="board")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    @Id
    @Column(name="board_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long boardId;

    @Column(nullable = false, length=100)
    private String title;

    @Column(nullable = false, length=256)
    private String content;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private int viewCount = 0;

    @Column(nullable = false, length=1)
    private char privacyFlag = 'N';

    @Column(nullable = false, length=1)
    private char deleteFlag = 'N';

    private LocalDateTime registDateTime;
    private LocalDateTime updateDateTime;

}
