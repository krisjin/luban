package org.luban.common.probe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * JVM相关的工具类
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/10/14
 * Time: 16:40
 */
public final class JVMUtil {

    private static final Logger logger = LoggerFactory.getLogger(JVMUtil.class);

    private JVMUtil() {
    }

    /**
     * 内存,GC统计
     */
    public static final class Memory {

        private Memory() {

        }

        /**
         * 获取内存管理bean
         */
        private static final MemoryMXBean MEMORY_BEAN = ManagementFactory.getMemoryMXBean();
        /**
         * 获取GC管理列表
         */
        private static final List<GarbageCollectorMXBean> GC_BEAN_LIST = ManagementFactory.getGarbageCollectorMXBeans();

        /**
         * Young GC垃圾收集器名称
         */
        private static final Set<String> YGC_COLLECOTR_NAME = new HashSet<String>();
        /**
         * Old GC垃圾收集器名称
         */
        private static final Set<String> OGC_COLLECTOR_NAME = new HashSet<String>();

        /**
         * Gc数据全局变量
         */
        private static volatile GcSnapshot preGcSnapshot = new GcSnapshot();

        static {
            //垃圾收集器的名称与jvm的实现及版本相关,如Hotspot jvm和JRockit的名称是不同的

            // ORACLE  HotSpot JVM Young gc names
            YGC_COLLECOTR_NAME.add("Copy");
            YGC_COLLECOTR_NAME.add("ParNew");
            YGC_COLLECOTR_NAME.add("PS Scavenge");

            // ORACLE  HotSpot JVM old gc names
            OGC_COLLECTOR_NAME.add("MarkSweepCompact");
            OGC_COLLECTOR_NAME.add("PS MarkSWeep");
            OGC_COLLECTOR_NAME.add("ConcurrentMarkSweep");
        }

        /**
         * 获取虚拟机中的空闲内存量
         *
         * @return
         * @see {@link Runtime#freeMemory()}
         */
        public static long freeMemory() {
            return Runtime.getRuntime().freeMemory();
        }

        /**
         * 获取虚拟机中的内存总量
         *
         * @return
         * @see {@link Runtime#totalMemory()}
         */
        public static long totalMemory() {
            return Runtime.getRuntime().totalMemory();
        }

        /**
         * 获取虚拟机试图使用的最大内存量
         *
         * @return
         * @see {@link Runtime#maxMemory()}
         */
        public static long maxMemory() {
            return Runtime.getRuntime().maxMemory();
        }

        /**
         * 获取堆内存使用情况
         *
         * @return
         * @see {@link MemoryMXBean#getHeapMemoryUsage()}
         */
        public static MemoryUsage heapUsage() {
            return MEMORY_BEAN.getHeapMemoryUsage();
        }

        /**
         * 获取非堆内存的使用情况
         *
         * @return
         * @see {@link MemoryMXBean#getNonHeapMemoryUsage()}
         */
        public static MemoryUsage nonHeapUsage() {
            return MEMORY_BEAN.getNonHeapMemoryUsage();
        }

        public GcSnapshot getGcSnapshot() {
            final GcSnapshot gcSnapshot = new GcSnapshot();
            for (GarbageCollectorMXBean gcBean : GC_BEAN_LIST) {
                final long collectionCount = gcBean.getCollectionCount();
                final long collectionTime = gcBean.getCollectionTime();
                final String collectorName = gcBean.getName();

                gcSnapshot.gcCount += collectionCount;
                gcSnapshot.gcTime += collectionTime;
                if (YGC_COLLECOTR_NAME.contains(collectorName)) {
                    gcSnapshot.youngGcCount += collectionCount;
                    gcSnapshot.youngGcTime += collectionTime;
                } else if (OGC_COLLECTOR_NAME.contains(collectorName)) {
                    gcSnapshot.fullGcCount += collectionCount;
                    gcSnapshot.fullGcTime += collectionTime;

                } else {
                    logger.warn("Unknown gc collector name:" + collectorName);
                }
            }

            //增量
            gcSnapshot.gcCountOffset = (gcSnapshot.gcCount - preGcSnapshot.gcCount) > 0 ? (gcSnapshot.gcCount - preGcSnapshot.gcCount) : 0;
            gcSnapshot.gcTimeOffset = (gcSnapshot.gcTime - preGcSnapshot.gcTime) > 0 ? (gcSnapshot.gcTime - preGcSnapshot.gcTime) : 0;
            gcSnapshot.youngGcCountOffset = (gcSnapshot.youngGcCount - preGcSnapshot.youngGcCount) > 0 ? (gcSnapshot.youngGcCount - preGcSnapshot.youngGcCount) : 0;
            gcSnapshot.youngGcTimeOffset = (gcSnapshot.youngGcTime - preGcSnapshot.youngGcTime) > 0 ? (gcSnapshot.youngGcTime - preGcSnapshot.youngGcTime) : 0;
            gcSnapshot.fullGcCountOffset = (gcSnapshot.fullGcCount - preGcSnapshot.fullGcCount) > 0 ? (gcSnapshot.fullGcCount - preGcSnapshot.fullGcCount) : 0;
            gcSnapshot.fullGcTimeOffset = (gcSnapshot.fullGcTime - preGcSnapshot.fullGcTime) > 0 ? (gcSnapshot.fullGcTime - preGcSnapshot.fullGcTime) : 0;
            //本次收集结果赋值上一次
            preGcSnapshot = gcSnapshot;
            return gcSnapshot;
        }
    }

    /**
     * GC状态的快照
     */
    public static final class GcSnapshot {

        /**
         * 总的GC时间
         */
        private long gcTime;

        /**
         * 总的GC时间差值
         */
        private long gcTimeOffset;

        /**
         * 总的GC次数
         */
        private long gcCount;

        /**
         * 总的GC次数差值
         */
        private long gcCountOffset;

        /**
         * Young GC 时间
         */
        private long youngGcTime;

        /**
         * Young GC的时间差值
         */
        private long youngGcTimeOffset;

        /**
         * Young Gc次数
         */
        private long youngGcCount;

        /**
         * Young Gc的次数差值
         */
        private long youngGcCountOffset;

        /**
         * Full Gc时间
         */
        private long fullGcTime;

        /**
         * Full Gc的时间差值
         */
        private long fullGcTimeOffset;

        /**
         * Full Gc次数
         */
        private long fullGcCount;

        /**
         * Full Gc的次数差值
         */
        private long fullGcCountOffset;

        public long getGcTime() {
            return gcTime;
        }

        public void setGcTime(long gcTime) {
            this.gcTime = gcTime;
        }

        public long getGcTimeOffset() {
            return gcTimeOffset;
        }

        public void setGcTimeOffset(long gcTimeOffset) {
            this.gcTimeOffset = gcTimeOffset;
        }

        public long getGcCount() {
            return gcCount;
        }

        public void setGcCount(long gcCount) {
            this.gcCount = gcCount;
        }

        public long getGcCountOffset() {
            return gcCountOffset;
        }

        public void setGcCountOffset(long gcCountOffset) {
            this.gcCountOffset = gcCountOffset;
        }

        public long getYoungGcTime() {
            return youngGcTime;
        }

        public void setYoungGcTime(long youngGcTime) {
            this.youngGcTime = youngGcTime;
        }

        public long getYoungGcTimeOffset() {
            return youngGcTimeOffset;
        }

        public void setYoungGcTimeOffset(long youngGcTimeOffset) {
            this.youngGcTimeOffset = youngGcTimeOffset;
        }

        public long getYoungGcCount() {
            return youngGcCount;
        }

        public void setYoungGcCount(long youngGcCount) {
            this.youngGcCount = youngGcCount;
        }

        public long getYoungGcCountOffset() {
            return youngGcCountOffset;
        }

        public void setYoungGcCountOffset(long youngGcCountOffset) {
            this.youngGcCountOffset = youngGcCountOffset;
        }

        public long getFullGcTime() {
            return fullGcTime;
        }

        public void setFullGcTime(long fullGcTime) {
            this.fullGcTime = fullGcTime;
        }

        public long getFullGcTimeOffset() {
            return fullGcTimeOffset;
        }

        public void setFullGcTimeOffset(long fullGcTimeOffset) {
            this.fullGcTimeOffset = fullGcTimeOffset;
        }

        public long getFullGcCount() {
            return fullGcCount;
        }

        public void setFullGcCount(long fullGcCount) {
            this.fullGcCount = fullGcCount;
        }

        public long getFullGcCountOffset() {
            return fullGcCountOffset;
        }

        public void setFullGcCountOffset(long fullGcCountOffset) {
            this.fullGcCountOffset = fullGcCountOffset;
        }

        @Override
        public String toString() {
            return "GcSnapshot{" +
                    "gcTime=" + gcTime +
                    ", gcTimeOffset" + gcTimeOffset +
                    ", gcCount=" + gcCount +
                    ", gcCountOffset" + gcCountOffset +
                    ", youngGcTime=" + youngGcTime +
                    ", youngGcTimeOffset" + youngGcTimeOffset +
                    ", youngGcCount=" + youngGcCount +
                    ", youngGcCountOffset" + youngGcCountOffset +
                    ", fullGcTime=" + fullGcTime +
                    ", fullGcTimeOffset" + fullGcTimeOffset +
                    ", fullGcCount=" + fullGcCount +
                    ", fullGcCountOffset" + fullGcCountOffset +
                    '}';
        }
    }

    /**
     * 线程,CPU使用率的监控
     */
    public static final class Sys {

        private Sys() {
        }

        private static final ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        private static final OperatingSystemMXBean mxBean = ManagementFactory.getOperatingSystemMXBean();
        private static final RuntimeMXBean rmBean = ManagementFactory.getRuntimeMXBean();
        private static final Method processCpuTimeMethod = getProcessCpuTimeMethod();
        private static final ProcessCPUUsage cpuUsage = new ProcessCPUUsage();

        private static class ProcessCPUUsage {
            //final int cpus = Runtime.getRuntime().availableProcessors();
            long prevUpTime = -1;
            long prevProcessCpuTime = -1L;
            volatile float cpuUsage = -1F;

            /**
             * 更新数据,此方法的调用应该有一定的时间间隔
             */
            public synchronized void update() {
                final long upTime = rmBean.getUptime();
                final long processCpuTime = getProcesCpuTime();
                if (processCpuTime < 0) {
                    return;
                }
                if (prevUpTime > 0L && upTime > prevUpTime) {
                    // elapsedCpu is in ns and elapsedTime is in ms.
                    long elapsedCpu = processCpuTime - prevProcessCpuTime;
                    long elapsedTime = upTime - prevUpTime;
                    cpuUsage = elapsedCpu / (elapsedTime * 10000F);
                } else {
                    logger.warn("prevUpTime=" + prevUpTime + " upTime=" + upTime);
                }
                this.prevUpTime = upTime;
                this.prevProcessCpuTime = processCpuTime;
            }
        }

        /**
         * 更新CPU使用率的数据,此方法的调用应该有一定的时间间隔
         */
        public static void updateCpuUsage() {
            cpuUsage.update();
        }

        /**
         * 取得Jvm的CPU使用率
         *
         * @return
         */
        public static float getCpuUsage() {
            return cpuUsage.cpuUsage;
        }


        /**
         * 取得jvm进程的cpu时间,单位ns
         *
         * @return <0,无法正确取得进程的时间;>=0 为
         */
        public static long getProcesCpuTime() {
            if (processCpuTimeMethod == null) {
                return -1;
            }
            try {
                Object value = processCpuTimeMethod.invoke(mxBean);
                if (value instanceof Number) {
                    return ((Number) value).longValue();
                } else {
                    logger.warn("Not a number value for " + value);
                }
            } catch (IllegalAccessException e) {
                logger.error("Can't get the ProcessCpuTime method for class ", e);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return -1;
        }

        /**
         * 线程数
         *
         * @return
         */
        public static long getThreadCount() {
            return threadBean.getThreadCount();
        }

        /**
         * 线程的峰值数
         *
         * @return
         */
        public static long getPeakThreadCount() {
            return threadBean.getPeakThreadCount();
        }

        /**
         * 取得Jvm运行的类名及其参数
         * 该方法的实现依赖sun jdk1.6
         *
         * @return
         */
        public static String getJavaCommand() {
            try {
                Class clazz = Class.forName("sun.misc.VMSupport");
                @SuppressWarnings("unchecked")
                Method m = clazz.getMethod("getAgentProperties");
                m.setAccessible(true);
                Properties properties = (Properties) m.invoke(clazz);
                return properties.getProperty("sun.java.command");
            } catch (ClassNotFoundException e) {
                logger.error("Fail get class ", e);
            } catch (NoSuchMethodException e) {
                logger.error("Fail get the method", e);
            } catch (InvocationTargetException e) {
                logger.error("Fail invoke the method", e);
            } catch (Exception e) {
                logger.error("Unkonwn error.", e);
            }
            return null;
        }

        /**
         * 取得Java进程的名称,格式为mainClassName+["-"+parameter[0]]
         *
         * @param javaCommand
         * @return
         */
        public static String getJavaProcessName(final String javaCommand) {
            if (javaCommand == null || javaCommand.isEmpty()) {
                return null;
            }
            String[] strings = javaCommand.split("\\s");

            // 取得Main class name
            String mainClassName = javaCommand;
            String firstParam = "";
            {
                //获取main class的名称,不包括包名
                if (strings.length > 0) {
                    String fullName = strings[0];
                    int dotIndex = fullName.lastIndexOf(".");
                    if (dotIndex > 0) {
                        mainClassName = fullName.substring(dotIndex + 1);
                    }
                }
                //如果有参数,那取第一个参数作为名称的额外标识
                if (strings.length > 1) {
                    firstParam = strings[1];
                }
            }
            if (firstParam.isEmpty()) {
                return mainClassName;
            } else {
                return mainClassName + "-" + firstParam;
            }
        }

        /**
         * 取得getProcessCpuTime方法,这个方法是Orace jvm的方法
         *
         * @return
         */
        private static Method getProcessCpuTimeMethod() {
            Class aClass = mxBean.getClass();
            try {
                @SuppressWarnings("unchecked")
                Method m = aClass.getMethod("getProcessCpuTime");
                m.setAccessible(true);
                return m;
            } catch (NoSuchMethodException e) {
                logger.error("Can't find the getProcessCpuTime method for class " + aClass, e);
            }
            return null;
        }
    }


}
