package org.luban.common.demo.os;

import org.luban.common.os.SystemInfo;

/**
 * User:krisjin
 * Date:2020-03-29
 */
public class SystemInfoDemo {

    public static void main(String[] args) {


        System.out.println(SystemInfo.getCores());
        System.out.println(SystemInfo.getUserHome());
        System.out.println(SystemInfo.getOsName());
    }

}
