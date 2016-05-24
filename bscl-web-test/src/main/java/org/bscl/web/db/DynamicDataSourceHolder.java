package org.bscl.web.db;

/**
 * User: shijingui
 * Date: 2016/5/23
 */
public class DynamicDataSourceHolder {
    public static final ThreadLocal<String> holder = new ThreadLocal<String>();

    public static void putDataSource(String name) {
        holder.set(name);
    }

    public static String getDataSouce() {
        return holder.get();
    }

}
