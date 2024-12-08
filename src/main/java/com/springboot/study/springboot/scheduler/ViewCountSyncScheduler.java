package com.springboot.study.springboot.scheduler;

import com.springboot.study.springboot.service.BoardViewService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ViewCountSyncScheduler {
    private final BoardViewService boardViewService;

    public ViewCountSyncScheduler(BoardViewService boardViewService) {
        this.boardViewService = boardViewService;
    }

    //10분 마다 캐시와 DB동기화
    @Scheduled(fixedRate = 6000)
    public void syncViewCounts() {
        boardViewService.syncWithDatabase();
    }
}
