package org.luban.common.hash;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kris
 * @date 2024/9/2
 */
public class AlarmTask implements Runnable {

    private AlarmConfig config;

    public AlarmTask(AlarmConfig config) {
        this.config = config;
    }

    public Boolean call() {
        List<Boolean> booleanList = new ArrayList<>();
        for (int i = 0; i < config.getInvokeCount(); i++) {
            try {
                //执行业务逻辑
                boolean result = true;//
                booleanList.add(result);
                Thread.sleep(config.getExecuteTime());
            } catch (InterruptedException e) {
                //打印失败日志调用
            }
        }
        //如果有一次调用失败，该阶段的探活为失败，比如有5次探活，失败一次
        for (Boolean state : booleanList) {
            if (!state) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     */
    @Override
    public void run() {
        List<Boolean> booleanList = new ArrayList<>();
        for (int i = 0; i < config.getInvokeCount(); i++) {
            try {
                //执行业务逻辑
                boolean result = true;//
                booleanList.add(result);
                Thread.sleep(config.getExecuteTime() * 1000);
            } catch (InterruptedException e) {
                //打印失败日志调用
            }
        }
        boolean success = true;
        //如果有一次调用失败，该阶段的探活为失败，比如有5次探活，失败一次
        for (Boolean state : booleanList) {
            if (!state) {
                //存储redis
                success = false;
            }
        }

        System.out.println("一分钟探活状态:" + success);
    }
}
