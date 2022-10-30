package org.luban.common.test;

import org.junit.Test;

public class UUIDTest {

    @Test
    public void changeY2FTest() {
        String ss= changeY2F(13543541.21);
        System.err.println(ss);
    }


    public static String changeY2F(Double amount) {
        String currency = amount.toString();
        int index = currency.indexOf(".");
        int length = currency.length();
        Long amLong = 0l;
        if (index == -1) {
            amLong = Long.valueOf(currency + "00");
        } else if (length - index >= 3) {
            amLong = Long.valueOf((currency.substring(0, index + 3)).replace(".", ""));
        } else if (length - index == 2) {
            amLong = Long.valueOf((currency.substring(0, index + 2)).replace(".", "") + 0);
        } else {
            amLong = Long.valueOf((currency.substring(0, index + 1)).replace(".", "") + "00");
        }
        return amLong.toString();
    }


}
