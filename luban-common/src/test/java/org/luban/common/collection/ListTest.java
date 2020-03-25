package org.luban.common.collection;

import org.luban.common.encrypt.Base64Util;
import org.junit.Test;

import java.io.*;

/**
 * User: krisjin
 * Date: 2016/6/7
 */
public class ListTest {

    @Test
    public void test1() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ListTest.class.getResourceAsStream("/pub_banned_words.txt")));
        try {
            FileOutputStream outputStream = new FileOutputStream("d:/b.txt");

            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                line = Base64Util.decode(line);
                outputStream.write(line.getBytes("UTF-8"));
            }

            outputStream.close();
            bufferedReader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
