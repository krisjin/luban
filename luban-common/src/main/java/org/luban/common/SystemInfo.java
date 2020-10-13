package org.luban.common;

import org.luban.common.io.Files;
import sun.misc.Unsafe;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User:krisjin
 * Date:2020-03-29
 */
public class SystemInfo {
    public static final String USER_HOME = "user.home";
    public static final String OS_NAME = "os.name";
    public static final String JAVA_IO_TMPDIR = "java.io.tmpdir";
    public static String ETC_CONFIG_INFO = "/etc/config_info";
    public static int CACHE_LINE_SIZE = 64;
    public static final Unsafe UNSAFE;
    // 缓存的PID
    protected static Long pid = null;
    protected static Pattern pattern = Pattern.compile("\"Cpuset\":\\s*\"(.*?)\"");
    protected static final String userHome = System.getProperty(USER_HOME);
    protected static final String osName = System.getProperty(OS_NAME);
    protected static final String tmpDir = System.getProperty(JAVA_IO_TMPDIR);

    static {

        try {
            final PrivilegedExceptionAction<Unsafe> action = new PrivilegedExceptionAction<Unsafe>() {
                public Unsafe run() throws Exception {
                    Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
                    theUnsafe.setAccessible(true);
                    return (Unsafe) theUnsafe.get(null);
                }
            };

            UNSAFE = AccessController.doPrivileged(action);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load unsafe", e);
        }
    }

    /**
     * 获取用户目录
     *
     * @return 用户目录
     */
    public static String getUserHome() {
        return userHome;
    }

    /**
     * 获取操作系统名称
     *
     * @return 操作系统名称
     */
    public static String getOsName() {
        return osName;
    }

    /**
     * 获取临时目录
     *
     * @return
     */
    public static String getTmpDir() {
        return tmpDir;
    }

    /**
     * 获取进程ID
     *
     * @return 进程ID
     */
    public static long getPid() {
        if (pid != null) {
            return pid;
        }
        String name = ManagementFactory.getRuntimeMXBean().getName();
        int indexOf = name.indexOf('@');
        if (indexOf > 0) {
            name = name.substring(0, indexOf);
        }
        pid = Long.parseLong(name);
        return pid;
    }

    /**
     * 获取CPU核数
     *
     * @return CPU核数
     */
    public static int getCores() {

        //cat /etc/config_info
        // {"Config": {"Cpuset": "1,2", "Memory": 4294967296}, "host_ip": "10.8.65.251"}
        File file = new File(ETC_CONFIG_INFO);
        if (file.exists() && file.length() > 0) {
            try {
                String text = Files.read(file);
                Matcher matcher = pattern.matcher(text);
                if (matcher.find()) {
                    text = matcher.group(1);
                    return text.split(",").length;
                }
            } catch (IOException ignored) {
            }
        }

        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * Get a handle on the Unsafe instance, used for accessing low-level concurrency
     * and memory constructs.
     *
     * @return The Unsafe
     */
    public static Unsafe getUnsafe() {
        return UNSAFE;
    }

    /**
     * 获取CPU缓存线大小
     *
     * @return
     */
    public static int getCacheLineSize() {
        return CACHE_LINE_SIZE;
    }

    /**
     * 获取长整形的字节数
     *
     * @return 长整形的字节数
     */
    public static int getLongBytes() {
        return 8;
    }

    /**
     * 获取长整形的字节数
     *
     * @return 长整形的字节数
     */
    public static int getIntBytes() {
        return 4;
    }
}
