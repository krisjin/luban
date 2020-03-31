package org.luban.common.time;

import java.util.concurrent.TimeUnit;

/**
 * 时间片段(毫秒)
 */
public class MilliPeriod implements Period {
    // 开始时间
    protected long startTime;
    // 终止时间
    protected long endTime;

    @Override
    public void begin() {
        startTime = SystemClock.now();
    }

    @Override
    public void end() {
        endTime = SystemClock.now();
    }

    @Override
    public long time() {
        return endTime - startTime;
    }

    @Override
    public void clear() {
        startTime = 0;
        endTime = 0;
    }

    @Override
    public TimeUnit getTimeUnit() {
        return TimeUnit.MILLISECONDS;
    }
}
