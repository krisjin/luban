package org.luban.common.io;

import org.luban.common.lang.Close;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * User:krisjin
 * Date:2020-03-29
 */
public class Files {

    /**
     * 从文件流里面拷贝数据
     *
     * @param in     输入流
     * @param out    输出流
     * @param start  输入位置
     * @param length 长度
     * @throws IOException
     */
    protected static void copy(final FileInputStream in, final FileOutputStream out, final long start,
                               final long length) throws IOException {
        if (in == null || out == null) {
            return;
        }
        FileChannel fci = in.getChannel();
        FileChannel fco = out.getChannel();
        try {
            fci.transferTo(start, length, fco);
            fco.force(false);
        } finally {
            Close.close(fci).close(fco);
        }
    }

    /**
     * 从文件流里面拷贝数据
     *
     * @param in       输入流
     * @param out      输出流
     * @param start    输入位置
     * @param length   长度
     * @param position 目标位置
     * @throws IOException
     */
    protected static void copy(final FileInputStream in, final RandomAccessFile out, final long start,
                               final long length, final long position) throws IOException {
        if (in == null || out == null) {
            return;
        }
        // 定位到目标位置
        out.seek(position);
        FileChannel fci = in.getChannel();
        FileChannel fco = out.getChannel();
        try {
            fci.transferTo(start, length, fco);
            fco.force(false);
        } finally {
            Close.close(fci).close(fco);
        }
    }

    /**
     * 从通道里面拷贝数据
     *
     * @param in     输入通道
     * @param out    输出通道
     * @param start  输入位置
     * @param length 长度
     * @throws IOException
     */
    protected static void copy(final FileChannel in, final FileChannel out, final long start, final long length) throws
            IOException {
        if (in == null || out == null) {
            return;
        }
        in.transferTo(start, length, out);
        out.force(false);
    }

    /**
     * 追加拷贝文件，并刷盘
     *
     * @param source 文件源
     * @param target 目标文件
     * @param start  开始
     * @param length 长度
     * @throws IOException
     */
    public static void copy(final File source, final File target, final long start, final long length) throws
            IOException {
        if (source == null || target == null) {
            return;
        }
        if (!target.exists()) {
            target.createNewFile();
        }
        if (length <= 0) {
            return;
        }
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(source);
            fos = new FileOutputStream(target, true);
            copy(fis, fos, start < 0 ? 0 : start, length);
        } finally {
            Close.close(fis).close(fos);
        }
    }

    /**
     * 拷贝文件数据到目标文件指定位置，并刷盘
     *
     * @param source   源文件
     * @param target   目标文件
     * @param start    源文件开始位置
     * @param length   长度
     * @param position 目标文件位置
     * @throws IOException
     */
    public static void copy(final File source, final File target, final long start, final long length,
                            final long position) throws IOException {
        if (source == null || target == null) {
            return;
        }
        if (!target.exists()) {
            target.createNewFile();
        }
        if (length <= 0) {
            return;
        }
        if (position < 0 || position == target.length()) {
            // 追加模式
            copy(source, target, start, length);
            return;
        }

        FileInputStream fis = null;
        RandomAccessFile raf = null;
        try {
            fis = new FileInputStream(source);
            raf = new RandomAccessFile(target, "rw");
            copy(fis, raf, start < 0 ? 0 : start, length, position);
        } finally {
            Close.close(fis).close(raf);
        }
    }

    /**
     * 追加拷贝文件，并刷盘
     *
     * @param source 文件源
     * @param target 目标文件
     * @param length 长度
     * @throws IOException
     */
    public static void copy(final File source, final File target, final long length) throws IOException {
        copy(source, target, 0, length);
    }

    /**
     * 拷贝覆盖文件，并刷盘
     *
     * @param source 文件源
     * @param target 目标文件
     * @throws IOException
     */
    public static void copy(final File source, final File target) throws IOException {
        if (source == null || target == null) {
            return;
        }
        if (!target.exists()) {
            target.createNewFile();
        }

        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(source);
            fos = new FileOutputStream(target);
            copy(fis, fos, 0, source.length());
        } finally {
            Close.close(fis).close(fos);
        }
    }

    /**
     * 拷贝数据流到文件指定位置，并刷盘
     *
     * @param is       输入
     * @param target   目标文件
     * @param length   数据长度
     * @param position 目标文件起始位置
     * @throws IOException
     */
    public static void copy(final InputStream is, final File target, final long length, final long position) throws
            IOException {
        if (is == null || target == null) {
            return;
        }
        if (!target.exists()) {
            target.createNewFile();
        }
        if (length <= 0) {
            return;
        }
        FileOutputStream fos = null;
        RandomAccessFile raf = null;
        try {
            if (is.getClass() == FileInputStream.class) {
                // 采用zeroCopy技术拷贝
                if (target.length() == position) {
                    // 追加模式
                    fos = new FileOutputStream(target, true);
                    copy((FileInputStream) is, fos, 0, length);
                } else {
                    // 随机拷贝
                    raf = new RandomAccessFile(target, "rw");
                    copy((FileInputStream) is, raf, 0, length, position);
                }
            } else {
                // 使用缓冲器拷贝
                raf = new RandomAccessFile(target, "rw");
                raf.seek(position);
                copy(is, raf, length);
                // 同步到硬盘
                raf.getFD().sync();
            }
        } finally {
            Close.close(fos).close(raf);
        }
    }

    /**
     * 流拷贝，并刷盘
     *
     * @param is     输入
     * @param os     输出
     * @param length 长度
     * @throws IOException
     */
    public static void copy(final InputStream is, final OutputStream os, final long length) throws IOException {
        copy(is, os, 0, length);
    }

    /**
     * 流拷贝，并刷盘
     *
     * @param is     输入
     * @param os     输出
     * @param start  源起始位置
     * @param length 长度
     * @throws IOException
     */
    public static void copy(final InputStream is, final OutputStream os, final long start, final long length) throws
            IOException {
        if (is == null || os == null || length == 0) {
            return;
        }
        if (is.getClass() == FileInputStream.class && os.getClass() == FileOutputStream.class) {
            // 采用zeroCopy技术拷贝
            copy((FileInputStream) is, (FileOutputStream) os, start, length);
        } else {
            long bytes = 0;
            if (start > 0) {
                bytes = is.skip(start);
                if (bytes < start) {
                    return;
                }
            }
            byte buffer[] = new byte[1024 * 4];
            int c = 0;
            bytes = 0;
            while (bytes < length && ((c = is.read(buffer, 0, (int) Math.min(buffer.length, length - bytes))) >= 0)) {
                os.write(buffer, 0, c);
                bytes += c;
            }
        }

    }

    /**
     * 流拷贝
     *
     * @param is     输入
     * @param os     输出
     * @param length 长度
     * @throws IOException
     */
    public static void copy(final InputStream is, final DataOutput os, final long length) throws IOException {
        if (is == null || os == null) {
            return;
        }
        byte buffer[] = new byte[1024 * 4];
        int c = 0;
        long pos = 0;
        while (pos < length && ((c = is.read(buffer, 0, (int) Math.min(buffer.length, length - pos))) >= 0)) {
            os.write(buffer, 0, c);
            pos += c;
        }
    }

    /**
     * move文件
     *
     * @param from 源文件
     * @param to   目标文件
     * @return 成功标示
     * @throws IOException
     */
    public static boolean move(final File from, final File to) throws IOException {
        if (from == null || to == null || !from.exists()) {
            return false;
        }

        // If a simple rename/mv does not work..
        // 删除目标文件
        to.delete();
        // 重命名
        if (!from.renameTo(to)) {
            // 不成功则采用流复制
            copy(from, to);
            // 删除
            from.delete();
        }
        return true;
    }

    /**
     * 递归删除目录和文件
     *
     * @param root
     */
    public static void deleteDirectory(final File root) {
        if (root == null || !root.exists()) {
            return;
        }
        if (root.isFile()) {
            root.delete();
        } else if (root.isDirectory()) {
            File[] children = root.listFiles();
            if (children != null) {
                for (File child : children) {
                    if (child.isFile()) {
                        child.delete();
                    } else if (child.isDirectory()) {
                        deleteDirectory(child);
                    }
                }
            }
            root.delete();
        }
    }

    /**
     * 创建目录
     *
     * @param directory
     * @return
     */
    public static boolean createDirectory(final File directory) {
        if (directory == null) {
            return false;
        }
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                if (!directory.exists()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 创建文件
     *
     * @param file 文件
     * @return 成功标示
     */
    public static boolean createFile(final File file) {
        if (file == null) {
            return false;
        }
        if (file.exists()) {
            return true;
        }
        try {
            if (file.createNewFile()) {
                return true;
            }
            return file.exists();
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 写文件
     *
     * @param file    文件
     * @param content 消息
     * @throws IOException
     */
    public static void write(final File file, final String content) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
            writer.newLine();
        } finally {
            Close.close(writer);
        }
    }

    /**
     * 读文件内容
     *
     * @param file 文件
     * @return 文件内容
     */
    public static String read(final File file) throws IOException {
        if (file == null || !file.exists() || file.length() < 1) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                if (count > 0) {
                    builder.append("\n");
                }
                builder.append(line);
                count++;
            }
        } catch (FileNotFoundException e) {
            //忽略文件不存在
        } finally {
            Close.close(reader);
        }
        return builder.toString();
    }

    /**
     * 获取文件名后缀，不包括"."
     *
     * @param name 文件名
     * @return 文件名后缀
     */
    public static String suffix(String name) {
        if (name == null) {
            return null;
        }
        name = name.trim();
        if (name.isEmpty()) {
            return null;
        } else if (name.charAt(0) == '.') {
            if (name.length() == 1) {
                return null;
            }
            name = name.substring(1);
        }
        return name;
    }

    /**
     * 构造目录
     *
     * @param files 文件路径数组
     * @return 全路径
     */
    public static File path(final String... files) {
        if (files == null || files.length == 0) {
            return null;
        }
        File result = null;
        for (String file : files) {
            if (result == null) {
                result = new File(file);
            } else {
                result = new File(result, file);
            }
        }
        return result;
    }
}
