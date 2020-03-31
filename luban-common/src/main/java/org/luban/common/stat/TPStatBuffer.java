package org.luban.common.stat;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * TP性能统计缓冲器，用于计算
 */
public class TPStatBuffer implements Serializable {
    // 默认矩阵长度，2的指数，便于取余数
    protected static final int LENGTH = 256;
    // 矩阵，最多存放length*length-1
    protected AtomicReferenceArray<AtomicIntegerArray> timer;
    // 成功处理的记录条数
    protected AtomicLong count = new AtomicLong(0);
    // 成功调用次数
    protected AtomicLong success = new AtomicLong(0);
    // 失败调用次数
    protected AtomicLong error = new AtomicLong(0);
    // 数据大小
    protected AtomicLong size = new AtomicLong(0);
    // 总时间
    protected AtomicLong time = new AtomicLong(0);
    // 最大时间
    protected int maxTime;
    // 矩阵的长度
    protected int length;
    // 2的指数
    protected int exponent;

    public TPStatBuffer() {
        this(LENGTH);
    }

    public TPStatBuffer(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("length must be greater than 0");
        }

        // 容量是2的指数
        int cap = 1;
        int exponent = 0;
        while (length > cap) {
            cap <<= 1;
            exponent++;
        }
        this.length = cap;
        this.exponent = exponent;
        this.timer = new AtomicReferenceArray<AtomicIntegerArray>(length);
        this.maxTime = length * length - 1;
    }

    /**
     * 清理
     */
    public void clear() {
        timer = new AtomicReferenceArray<AtomicIntegerArray>(length);
        success.set(0);
        error.set(0);
        size.set(0);
        time.set(0);
    }

    /**
     * 成功调用，增加TP计数
     *
     * @param count 处理的记录条数
     * @param size  数据包大小
     * @param time  时间
     */
    public void success(final int count, final long size, final int time) {
        if (time < 0) {
            // 做性能统计时间不可能为负数
            return;
        }
        success.incrementAndGet();
        if (count > 0) {
            this.count.addAndGet(count);
        }
        if (size > 0) {
            this.size.addAndGet(size);
        }
        if (time > 0) {
            this.time.addAndGet(time);
        }
        int ptime = time;
        int maxIndex = length - 1;

        if (ptime > maxTime) {
            ptime = maxTime;
        }
        int i = ptime >> exponent;
        int j = ptime & maxIndex;
        AtomicIntegerArray v = timer.get(i);
        if (v == null) {
            v = new AtomicIntegerArray(length);
            if (!timer.compareAndSet(i, null, v)) {
                v = timer.get(i);
            }
        }
        v.addAndGet(j, 1);
    }

    /**
     * 出错，增加TP计数
     */
    public void error() {
        error.incrementAndGet();
    }

    /**
     * 获取性能统计
     *
     * @return 性能统计
     */
    public TPStat getTPStat() {
        TPStat stat = new TPStat();
        stat.setSuccess(success.get());
        stat.setError(error.get());
        stat.setCount(count.get());
        stat.setTime(time.get());
        stat.setSize(size.get());

        if (stat.getSuccess() <= 0) {
            return stat;
        }

        int min = -1;
        int max = -1;
        int tp999 = (int) Math.floor(stat.getSuccess() * 99.9 / 100);
        int tp99 = (int) Math.floor(stat.getSuccess() * 99.0 / 100);
        int tp90 = (int) Math.floor(stat.getSuccess() * 90.0 / 100);
        int tp50 = (int) Math.floor(stat.getSuccess() * 50.0 / 100);

        int count;
        int prev = 0;
        int next = 0;
        int time;
        AtomicIntegerArray v;
        for (int i = 0; i < length; i++) {
            v = timer.get(i);
            if (v == null) {
                continue;
            }
            for (int j = 0; j < length; j++) {
                count = v.get(j);
                if (count <= 0) {
                    continue;
                }
                time = i * length + j;
                if (min == -1) {
                    min = time;
                }
                if (max == -1 || time > max) {
                    max = time;
                }
                next = prev + count;
                if (prev < tp50 && next >= tp50) {
                    stat.setTp50(time);
                }
                if (prev < tp90 && next >= tp90) {
                    stat.setTp90(time);
                }
                if (prev < tp99 && next >= tp99) {
                    stat.setTp99(time);
                }
                if (prev < tp999 && next >= tp999) {
                    stat.setTp999(time);
                }
                prev = next;
            }
        }
        stat.setMin(min);
        stat.setMax(max);
        return stat;
    }

}
