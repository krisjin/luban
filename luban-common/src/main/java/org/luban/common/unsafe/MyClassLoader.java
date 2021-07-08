package org.luban.common.unsafe;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * User: krisjin
 * Date: 2021/5/19
 */
public class MyClassLoader extends URLClassLoader {


    private static boolean canCloseJar;
    private List<JarURLConnection> cachedJarFiles = new ArrayList<>();

    static {
        // 1.7之后可以直接调用close方法关闭打开的jar，
        // 需要判断当前运行的环境是否支持close方法，
        // 如果不支持，需要缓存，避免卸载模块后无法删除jar
        try {
            URLClassLoader.class.getMethod("close");
            canCloseJar = true;
        } catch (Exception e) {
            canCloseJar = false;
        }
    }

    public MyClassLoader() {
        super(new URL[0], findParentClassLoader());
    }

    private static ClassLoader findParentClassLoader() {
        ClassLoader parent = Thread.currentThread().getContextClassLoader();
        System.out.println("context loader:" + parent);
        if (parent != null) {
            parent = parent.getParent(); // use appClassLoader rather than LaunchedClassLoader
        }
        if (parent == null) {
            parent = ClassLoader.getSystemClassLoader();
        }

        return parent;
    }


    public void addURL(URL url) {
        if (!canCloseJar) {
            try {
                // 打开并缓存文件url连接
                URLConnection uc = url.openConnection();
                if (uc instanceof JarURLConnection) {
                    uc.setUseCaches(true);
                    ((JarURLConnection) uc).getManifest();
                    cachedJarFiles.add((JarURLConnection) uc);
                }
            } catch (Exception e) {
                // ignore
            }
        }
        super.addURL(url);
    }

    public void close() throws IOException {
        if (canCloseJar) {
            try {
                super.close();
            } catch (IOException ioe) {
                // ignore
            }
        } else {
            for (JarURLConnection conn : cachedJarFiles) {
                conn.getJarFile().close();

            }
            cachedJarFiles.clear();
        }
    }
}
