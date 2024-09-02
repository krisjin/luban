package org.luban.common.hash;

import com.alibaba.fastjson.JSONObject;

/**
 * @author kris
 * @date 2024/9/2
 */
public class AlarmConfig {

    /**
     * 探活时间段,单位秒
     */
    private String probeTime;
    /**
     * 调用次数
     */
    private int invokeCount;
    /**
     * 执行时间,单位秒
     */
    private int executeTime;
    /**
     * 告警阈值，例如80%
     */
    private String alarmThreshold;

    public String getProbeTime() {
        return probeTime;
    }

    public void setProbeTime(String probeTime) {
        this.probeTime = probeTime;
    }

    public int getInvokeCount() {
        return invokeCount;
    }

    public void setInvokeCount(int invokeCount) {
        this.invokeCount = invokeCount;
    }

    public String getAlarmThreshold() {
        return alarmThreshold;
    }

    public void setAlarmThreshold(String alarmThreshold) {
        this.alarmThreshold = alarmThreshold;
    }

    public int getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(int executeTime) {
        this.executeTime = executeTime;
    }

    public static void main(String[] args) {
        AlarmConfig alarmConfig = new AlarmConfig();
        alarmConfig.setProbeTime("60");
        alarmConfig.setInvokeCount(5);
        alarmConfig.setAlarmThreshold("80");
        alarmConfig.setExecuteTime(12);
        System.out.println(JSONObject.toJSONString(alarmConfig));
    }
}
