package net.common.utils;

import com.google.common.base.Joiner;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * User: shijingui
 * Date: 2016/3/25
 */
public class FileTest {


    public static void main(String[] args) throws IOException {
        List<String> lines = FileUtils.readLines(new File("d:/nums.txt"));
        for (String s : lines) {
        }
        String line = Joiner.on(",").join(lines);
        System.out.println(line);
    }
}
