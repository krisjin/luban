package org.luban.common.test;

import org.junit.Test;
import org.luban.common.io.GzipUtil;

import java.io.IOException;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/9/1
 * Time: 11:21
 */
public class GzipUtilTest {
    private static String d = "user_imp_cnt_1days:0.0,user_imp_cnt_3days:0.0,user_imp_cnt_7days:0.0,user_imp_cnt_15days:23.0,user_imp_cnt_30days:86.0,user_click_cnt_1days:0.0,user_click_cnt_3days:0.0,user_click_cnt_7days:0.0,user_click_cnt_15days:0.0,user_click_cnt_30days:2.0,user_click_ratio_1days:0.0,user_click_ratio_3days:0.0,user_click_ratio_7days:0.0,user_click_ratio_15days:0.0,user_click_ratio_30days:0.023255813953488372,user_click_datediff_counts:1.0,user_icon_avg_click_day:1.0,last_click_days_till_now:24.0,user_icon_click_value_1days:0.0,user_icon_click_value_3days:0.0,user_icon_click_value_7days:0.0,user_icon_click_value_15days:0.0,user_icon_click_value_30days:0.4,user_icon_click_cnt_1days:0.0,user_icon_click_cnt_3days:0.0,user_icon_click_cnt_7days:0.0,user_icon_click_cnt_15days:0.0,user_icon_click_cnt_30days:2.0,user_icon_click_cnt_value_1days:0.0,user_icon_click_cnt_value_3days:0.0,user_icon_click_cnt_value_7days:0.0,user_icon_click_cnt_value_15days:0.0,user_icon_click_cnt_value_30days:1.0,user_icon_avg_click_value_1days:0.0,user_icon_avg_click_value_3days:0.0,user_icon_avg_click_value_7days:0.0,user_icon_avg_click_value_15days:0.0,user_icon_avg_click_value_30days:0.4,user_icon_per_click_value_1days:0.0,user_icon_per_click_value_3days:0.0,user_icon_per_click_value_7days:0.0,user_icon_per_click_value_15days:0.0,user_icon_per_click_value_30days:0.2,user_icon_click_value_rate_1days:0.0,user_icon_click_value_rate_3days:0.0,user_icon_click_value_rate_7days:0.0,user_icon_click_value_rate_15days:0.0,user_icon_click_value_rate_30days:0.5,user_icon_datediff_order_counts:0.0,user_icon_last_value_days_till_now:0.0,user_icon_value_datediff_avg_days:0.0_2020-03-16";


    @Test
    public void test() {
        String name = "test";
        try {
            byte[] nameCompress = GzipUtil.compress(d.getBytes());
            byte[] nameDecompress = GzipUtil.decompress(nameCompress);


            System.out.println("origin:" + d.getBytes().length);
            System.out.println("compress:" + nameCompress.length);
            System.out.print("decompress:" + nameDecompress.length);

            String s1 = new String(nameDecompress);
            System.out.println(s1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
