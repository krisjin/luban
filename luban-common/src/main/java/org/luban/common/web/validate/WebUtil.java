package org.luban.common.web.validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/8/7
 * Time: 17:54
 */
public class WebUtil {

    public static boolean isMobile(String str) {
        Pattern p;
        Matcher m;
        boolean b;
        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$");
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    public static void main(String[] args) {
        String tel = "18810489320";

        boolean ret = isMobile(tel);
        System.out.println(ret);

    }

}
