package com.multicampus.topicsation.schedule;

import com.multicampus.topicsation.repository.ILoginDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Autowired
    ILoginDAO iLoginDAO;

    @Scheduled(cron = "0 0 3 * * ?") // 매일 새벽 세시에 실행
    public void deleteUnauthenticatedUsers() {
        iLoginDAO.deleteUnauthenticatedUsers();
        System.out.println("비인증 유저 삭제!!!");
    }
}
