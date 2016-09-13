package org.bscl.common;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: shijingui
 * Date: 2016/9/13
 */
public class RandomTest {

    @Test
    public void test() {
        List<String> serverList = new ArrayList<String>();
        serverList.add("ServerA");
        serverList.add("ServerB");
        serverList.add("ServerC");
        serverList.add("ServerD");
        Map<String, AtomicInteger> counter = new HashMap<String, AtomicInteger>();
        Random random = new Random();

        for (int i = 0; i < 10000; i++) {
            String server = serverList.get(random.nextInt(serverList.size()));
            AtomicInteger counts = counter.get(server);
            if (null != counts) {
                counts.compareAndSet(counts.get(), counts.incrementAndGet());
                counter.put(server, counts);
            } else {
                counter.put(server, new AtomicInteger(1));
            }
        }

        for (Map.Entry<String, AtomicInteger> entry : counter.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue().get());
        }
    }

    @Test
    public void test2() {
        Random random = new Random();
        for (int i = 0; i <= 10; i++) {
            int randomNum = random.nextInt(400);
            System.out.println(randomNum);
        }
    }
}
