package org.luban.common.test;

import org.luban.common.encrypt.UuidUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/9/1
 * Time: 15:11
 */
public class UUIDTest implements Runnable {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 6; i++) {
            executorService.execute(new UUIDTest());
        }
    }

    private void printUuid() {
        System.out.println(UuidUtil.genTerseUuid());
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            printUuid();
        }
    }
}
