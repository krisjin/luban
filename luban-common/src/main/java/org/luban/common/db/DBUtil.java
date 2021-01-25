package org.luban.common.db;

import com.google.common.base.CaseFormat;
import org.luban.common.date.DateTimeUtil;

import java.lang.reflect.Field;

/**
 * @author krisjin
 * @date 2021/1/25
 */
public class DBUtil {

    public static void main(String[] args) {

        Field[] fields = DateTimeUtil.class.getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        StringBuilder lowerStr = new StringBuilder();

        for (Field field : fields) {
            String name = field.getName();
            sb.append(name + ",\n");
            String resultStr = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
            lowerStr.append(resultStr + ",\n");
        }

        System.err.println(sb);
    }
}
