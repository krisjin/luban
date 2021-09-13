package org.luban.common;

/**
 * User:krisjin
 * Date:2020-04-16
 */
public class StringUtil {

    private static boolean isNullOrEmpty(String str) {
        if (null == str) {
            return true;
        } else {
            if (str.isEmpty()) {
                return true;
            } else {
                return false;
            }
        }
    }


    public static void addArgs(String argsValue) {


        String args=null;

        if (args == null || args.isEmpty()) {
            args = argsValue;
        } else {
            args = args + ";" + argsValue;
        }

        System.err.println(args);
    }

    public static void main(String[] args) {
        String s = ",";

        addArgs("222");
    }
}
