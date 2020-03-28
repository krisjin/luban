package org.luban.common.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * User:krisjin
 * Date:2020-03-28
 */
public class NamedThreadFactory {

    private static ExecutorService executor = new ThreadPoolExecutor(50, 50, 30L,
            TimeUnit.SECONDS, new SynchronousQueue(), new org.luban.common.concurrent.NamedThreadFactory("test"));

    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    System.err.println(Thread.currentThread().getName() + " say");
                }
            });
        }

        executor.shutdownNow();
    }
}
