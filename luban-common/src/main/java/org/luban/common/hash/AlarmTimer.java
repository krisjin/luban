package org.luban.common.hash;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author kris
 * @date 2024/9/2
 */
public class AlarmTimer {

    public static AtomicInteger counter = new AtomicInteger(0);
    public static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2, new NamedThreadFactory("HealthCheck-Task"));

    public static void executeTask(Runnable task) {
        scheduledExecutorService.scheduleWithFixedDelay(task, 5, 2, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {


        executeTask(null);
    }

}
