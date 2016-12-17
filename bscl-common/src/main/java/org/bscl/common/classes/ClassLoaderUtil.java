package org.bscl.common.classes;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类加载工具类
 * User: shijingui
 * Date: 2016/12/4
 */
public class ClassLoaderUtil {
    private final static String CLAZZ_SUFFIX = ".class";
    private ClassLoader classLoader;
    private static Set<Class> primitiveSet = new HashSet<Class>();

    static {
        primitiveSet.add(Integer.class);
        primitiveSet.add(Long.class);
        primitiveSet.add(Float.class);
        primitiveSet.add(Byte.class);
        primitiveSet.add(Short.class);
        primitiveSet.add(Double.class);
        primitiveSet.add(Character.class);
        primitiveSet.add(Boolean.class);
    }

    public void loadPath(String jarPath) {
        try {
            File jarFile = new File(jarPath);

            File[] jarFiles = jarFile.listFiles();
            URL[] jarFilePathArr = new URL[jarFiles.length];
            int i = 0;
            for (File jarfile : jarFiles) {
                String jarName = jarfile.getName();
                if (jarName.indexOf(".jar") < 0) {
                    continue;
                }
                String jarFilePath = "file:\\" + jarPath + File.separator + jarName;
                jarFilePathArr[i] = new URL(jarFilePath);
                i++;
            }
            classLoader = new URLClassLoader(jarFilePathArr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param jarName jar包名字 完整路径
     * @throws
     * @Title loadJar
     * @Description 遍历jar包下的类
     * @Author weizhi2018
     */
    public void loadJar(String jarName) {
        if (jarName.indexOf(".jar") < 0) {
            return;
        }
        try {
            JarFile jarFile = new JarFile(jarName);
            Enumeration<JarEntry> em = jarFile.entries();
            while (em.hasMoreElements()) {
                JarEntry jarEntry = em.nextElement();
                String clazzFile = jarEntry.getName();

                if (!clazzFile.endsWith(CLAZZ_SUFFIX)) {
                    continue;
                }
                String clazzName = clazzFile.substring(0, clazzFile.length() - CLAZZ_SUFFIX.length()).replace('/', '.');
                System.out.println(clazzName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param clazzName 类名字
     * @return
     * @throws
     * @Title loadClass
     * @Description 通过类加载器实例化
     * @Author weizhi2018
     */
    public Object loadClassAndInstance(String clazzName) {
        if (this.classLoader == null) {
            return null;
        }
        Class clazz = null;
        try {
            clazz = this.classLoader.loadClass(clazzName);
            Object obj = clazz.newInstance();
            return obj;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Class loadClass(String clazzName) {
        if (this.classLoader == null) {
            return null;
        }
        Class clazz = null;
        try {
            clazz = this.classLoader.loadClass(clazzName);
            return clazz;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 得到当前ClassLoader
     *
     * @return ClassLoader
     */
    public static ClassLoader getCurrentClassLoader() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = ClassLoaderUtil.class.getClassLoader();
        }
        return cl == null ? ClassLoader.getSystemClassLoader() : cl;
    }

    /**
     * 根据类名加载Class
     *
     * @param className  类名
     * @param initialize 是否初始化
     * @return Class
     * @throws ClassNotFoundException 找不到类
     */
    public static Class forName(String className, boolean initialize)
            throws ClassNotFoundException {
        return Class.forName(className, initialize, getCurrentClassLoader());
    }


    /**
     * 得到当前ClassLoader
     *
     * @param clazz 某个类
     * @return ClassLoader
     */
    public static ClassLoader getClassLoader(Class<?> clazz) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader != null) {
            return loader;
        }
        if (clazz != null) {
            loader = clazz.getClassLoader();
            if (loader != null) {
                return loader;
            }
            return clazz.getClassLoader();
        }
        return ClassLoader.getSystemClassLoader();
    }


    /**
     * 实例化一个对象(只检测默认构造函数，其它不管）
     *
     * @param clazz 对象类
     * @param <T>   对象具体类
     * @return 对象实例
     * @throws Exception 没有找到方法，或者无法处理，或者初始化方法异常等
     */
    public static <T> T newInstance(Class<T> clazz) throws Exception {
        if (primitiveSet.contains(clazz)) {
            return null;
        }
        if (clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers())) {
            Constructor constructorList[] = clazz.getDeclaredConstructors();
            Constructor defaultConstructor = null;
            for (Constructor con : constructorList) {
                if (con.getParameterTypes().length == 1) {
                    defaultConstructor = con;
                    break;
                }
            }
            if (defaultConstructor != null) {
                if (defaultConstructor.isAccessible()) {
                    return (T) defaultConstructor.newInstance(new Object[]{null});
                } else {
                    try {
                        defaultConstructor.setAccessible(true);
                        return (T) defaultConstructor.newInstance(new Object[]{null});
                    } finally {
                        defaultConstructor.setAccessible(false);
                    }
                }
            } else {
                throw new Exception("The " + clazz.getCanonicalName() + " has no default constructor!");
            }
        }
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            if (constructor.isAccessible()) {
                throw new Exception("The " + clazz.getCanonicalName() + " has no default constructor!", e);
            } else {
                try {
                    constructor.setAccessible(true);
                    return constructor.newInstance();
                } finally {
                    constructor.setAccessible(false);
                }
            }
        }
    }

    public static void main(String[] args) {
        String libPath = System.getProperty("user.dir") + File.separator + "lib";

        ClassLoaderUtil deployJar = new ClassLoaderUtil();

        deployJar.loadPath(libPath);
        String jarPath = libPath + File.separator + "bscl-common.jar";

        deployJar.loadJar(jarPath);

        Class clazz = deployJar.loadClass("org.bscl.common.net.NetUtil");
        deployJar.loadClassAndInstance("org.bscl.common.net.NetUtil");

    }
}
