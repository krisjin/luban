package org.luban.common;

import org.xerial.snappy.Snappy;

import java.io.IOException;

/**
 * User:krisjin
 * Date:2019/2/20
 *  
 */
public class SnappyHelper {


    public static byte[] compressHtml(String html) {
        try {

            return Snappy.compress(html.getBytes("UTF-8"));

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decompressHtml(byte[] bytes) {
        try {
            return new String(Snappy.uncompress(bytes));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
