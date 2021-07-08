package org.luban.common.unsafe;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * User: krisjin
 * Date: 2021/5/19
 */
public class AA {

    public static void main(String[] args) throws Exception {
        URL url = new URL("file:/usr/gitrep/fastjson-1.2.75.jar");
        String className = "com.alibaba.fastjson.JSONArray";

        URLClassLoader classLoaderLeft = new URLClassLoader(new URL[]{url}, Thread.currentThread().getContextClassLoader());
        Class<?> leftClass = classLoaderLeft.loadClass(className);
        Class<?> leftClassOth = classLoaderLeft.loadClass(className);

        URLClassLoader classLoaderRight = new URLClassLoader(new URL[]{url}, Thread.currentThread().getContextClassLoader());
        Class<?> rightClass = classLoaderRight.loadClass(className);

    }

}
