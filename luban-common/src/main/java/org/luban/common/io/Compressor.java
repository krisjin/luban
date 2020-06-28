package org.luban.common.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 压缩器
 */
public interface Compressor {

    /**
     * 压缩
     *
     * @param buf    缓冲器
     * @param offset 偏移量
     * @param size   长度
     * @param out    输出流
     * @throws IOException
     */
    void compress(byte[] buf, int offset, int size, OutputStream out) throws IOException;

    /**
     * 解压缩
     *
     * @param buf    缓冲器
     * @param offset 偏移量
     * @param size   长度
     * @param out    输出流
     * @throws IOException
     */
    void decompress(byte[] buf, int offset, int size, OutputStream out) throws IOException;
}
