package com.springboot.study.springboot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="reply")
@Getter
@Setter
public class Reply {
    @Id
    @Column(name="reply_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long replyId;

    @Column(nullable = false)
    private long boardId;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private char privacyFlag = 'N';

    private LocalDateTime registDateTime;
    private LocalDateTime updateDateTime;
}
