package net.common.utils.ztest;

import java.util.ArrayList;
import java.util.List;

/**
 * -verbose:gc -Xloggc:gc.log -XX:+PrintGCDetails  -XX:+PrintGCTimeStamps -XX:+PrintHeapAtGC  -XX:+PrintGCApplicationStoppedTime
 * -XX:-PrintTenuringDistribution
 * -XX:+PrintCommandLineFlags
 * -XX:MaxTenuringThreshold=15
 * -XX:ParallelGCThreads=4
 * -XX:SurvivorRatio=8
 * -XX:NewRatio=8
 * -XX:+UseConcMarkSweepGC
 * -XX:+HeapDumpOnOutOfMemoryError
 *-XX:HeapDumpPath=./java_pid<pid>.hprof
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/9/2
 * Time: 11:42
 */
public class GCTest {


    public static void main(String[] args) {
        List<Object> obj = new ArrayList<Object>();
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            obj.add(new Object());
        }

    }
}
