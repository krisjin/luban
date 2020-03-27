package org.luban.common.test;

import org.luban.common.web.html.HtmlUtil;
import org.junit.Test;
import org.luban.common.web.html.HtmlUtil;

/**
 * User: krisjin
 * Date: 2016/6/21
 */
public class HtmlUtilTest {

    @Test
    public void intercept() {

        String str = "ddd你好吗啊啊";
        String str2 = HtmlUtil.interceptLen(str, 5);
        System.out.println(str2);
    }
}
