package org.luban.event;

/**
 * User: krisjin
 * Date: 2021/8/31
 */
public class C {

    public static void main(String[] args) {
        String s1 = "1.0";
        String s2 = "1.1";
        if (s1.compareTo(s2) > 0) {
            System.err.println("s1>2");
        }
        if (s1.compareTo(s2) < 0) {
            System.err.println("s1<s2");
        }
    }
}
