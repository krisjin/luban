package org.bscl.common.encrypt;

import org.apache.xmlbeans.impl.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * User: shijingui
 * Date: 2016/6/6
 */
public class Base64Util {

    public static String encode(final String str) {
        byte[] bytes = null;
        try {
            bytes = Base64.encode(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new String(bytes);
    }

    public static String decode(final String str) {
        String s = null;
        try {
            s = new String(Base64.decode(str.getBytes()), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

}
