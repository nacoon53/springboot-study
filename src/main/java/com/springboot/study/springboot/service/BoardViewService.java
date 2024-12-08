package com.springboot.study.springboot.service;

import com.springboot.study.springboot.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BoardViewService {
    private final ConcurrentHashMap<Long, AtomicInteger> viewCache = new ConcurrentHashMap<>();
    private final BoardRepository boardRepository;

    public BoardViewService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    //조회수 증가
    public void incrementViewCount(Long boardId) {
        viewCache.computeIfAbsent(boardId, id -> new AtomicInteger(0)).incrementAndGet();
    }

    //조회수 가져오기
    public int getViewCount(Long boardId) {
        return viewCache.getOrDefault(boardId, new AtomicInteger(0)).get();
    }

    //캐시를 DB에 동기화
    public void syncWithDatabase() {
        viewCache.forEach((boardId, count) -> {
            int currentCount = count.getAndSet(0); //캐시 초기화
            if(currentCount > 0) {
                boardRepository.incrementViewCount(boardId, currentCount);
            }
        });
    }

}
