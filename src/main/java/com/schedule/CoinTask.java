package com.schedule;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by Robin Wang  on 2018/4/24.
 */
@Configuration
@EnableScheduling
public class CoinTask {
    public void querySub(){
        System.out.println("执行定时任务...");
    }
}
