package org.luban.common.demo.thread;


import org.luban.common.concurrent.Threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * User:krisjin
 * Date:2020-03-30
 */
public class ThreadTest {


    public static void main(String[] args) {
        final List<Callable> subTaskList = new ArrayList();
        for (int i = 0; i < 10; i++) {
            subTaskList.add(new SubTask("t-" + i));
        }

        try {
            List<Boolean> list = Threads.invoke(subTaskList, 29, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    static class SubTask implements Callable<Boolean> {
        private String newName;

        public SubTask(String newName) {
            this.newName = newName;
        }

        public Boolean call() throws Exception {
            System.out.println("--" + newName);
            return true;
        }
    }
}
