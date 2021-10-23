package org.luban.common;

import com.google.common.collect.Lists;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author shijingui on 2021/9/24
 */
public class ListTest {
    public static void main(String[] args) {
        List<String> contentList = new ArrayList<>();
        contentList.add("11");
//        contentList.add("12");
//        contentList.add("13");
//        contentList.add("14");
//        contentList.add("15");

        List<List<String>> a = Lists.partition(contentList, 20);

        String dt = "2021-09";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");


        dt= dt.replace("-","");
        System.err.println(dt);

        try {
            Date date = sdf.parse(dt);


            System.err.println(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        sdf.format(new Date());


        System.err.println();
    }
}
