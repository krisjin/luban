package org.luban.common.hash;

import com.alibaba.fastjson.JSONObject;

import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
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
        AlarmConfig alarmConfig = new AlarmConfig();
        alarmConfig.setProbeTime("60");
        alarmConfig.setInvokeCount(5);
        alarmConfig.setExecuteTime(12);
        alarmConfig.setAlarmThreshold("80");
        System.out.println(JSONObject.toJSONString(alarmConfig));

        AlarmTask alarmTask = new AlarmTask(alarmConfig);
        executeTask(alarmTask);

    }

}
