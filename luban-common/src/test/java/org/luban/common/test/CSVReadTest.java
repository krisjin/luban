package org.luban.common.test;

import org.luban.common.io.FileUtil;
import org.luban.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * User:krisjin
 * Date:2020-06-28
 */
public class CSVReadTest {

    public static void main(String[] args) {
        String type = "libsvm";

        try {
            List<String> data = Files.readList(new File("/usr/local/gitrep/luban/luban-common/src/test/resources/fin.csv"));
            File outFile = new File("/usr/local/bi/test.libsvm");
            int size = 0;

            for (String s : data) {
                String[] fieldArr = s.split(",");
                StringBuilder sb = new StringBuilder();
                size = fieldArr.length;

                for (int i = 0; i < size; i++) {
                    if (i == 0) {//label
                        sb.append(fieldArr[i] + " ");
                    } else if (i == size - 1) {
                        sb.append(i + ":" + fieldArr[i] + " \n");

                    } else {
                        sb.append(i + ":" + fieldArr[i]).append(" ");
                    }
                }
                FileUtil.string2File(outFile, sb.toString(), true);
            }


            System.err.println(data.size());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
