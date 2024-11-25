package com.springboot.study.springboot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="user")
@Getter
@Setter
public class User {
    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(nullable = false, length=100)
    private String email;

    @Column(nullable = false, length=256)
    private String password;

    @Column(nullable = false, length=50)
    private String name;

    @Column(nullable = false, length=13)
    private String phone;

    @Column(nullable = false, length=1)
    private char deleteFlag = 'N';

    @Column
    private LocalDateTime lastLoginDate;

    private LocalDateTime registDateTime;
    private LocalDateTime updateDateTime;
}
