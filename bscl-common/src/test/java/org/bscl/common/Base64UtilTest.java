package org.bscl.common;

import org.bscl.common.encrypt.Base64Util;
import org.junit.Test;

import java.io.*;
import java.util.HashSet;

/**
 * User: shijingui
 * Date: 2016/6/6
 */
public class Base64UtilTest {

    @Test
    public void decode() {
        String str = Base64Util.decode("5L2g5aW9");
        System.out.println(str);
    }

    @Test
    public void decodeFile() {
        String decodeFile = "d:/sens/sens1_base64.txt";
        String outputFile = "d:/sens/sens1.txt";
        HashSet set = new HashSet<String>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(decodeFile), "UTF-8"));
            FileWriter fw = new FileWriter(outputFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fw);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = Base64Util.decode(line).replace("\n", "");
                set.add(line);
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void decodeFile2() {
        String decodeFile = "d:/sens/sens2_base64.txt";
        HashSet set = new HashSet<String>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(decodeFile), "UTF-8"));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = Base64Util.decode(line).replace("\n", "");
                set.add(line);

            }
            line = null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void encode() {
        System.out.println(Base64Util.encode("司马系列气狗购买"));
    }

    @Test
    public void encodeFile() {
        String encodeFile = "d:/sens/sens2.txt";
        String outputFile = "d:/sens/sens2_base64.txt";
        HashSet set = new HashSet();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(encodeFile),"UTF-8"));
            FileWriter fw = new FileWriter(outputFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fw);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                set.add(line);
                line = Base64Util.encode(line);
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
