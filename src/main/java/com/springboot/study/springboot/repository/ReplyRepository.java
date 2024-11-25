package com.springboot.study.springboot.repository;

import com.springboot.study.springboot.entity.Reply;
import com.springboot.study.springboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

}
