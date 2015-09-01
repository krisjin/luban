package net.common.utils.gzip;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/9/1
 * Time: 11:19
 */
public final class GzipUtil {

    private static final int BUFFER = 1024 * 4;

    private GzipUtil() {
    }

    /**
     * 压缩
     *
     * @param data
     * @return
     * @throws java.io.IOException
     */
    public static byte[] compress(byte[] data) throws IOException {
        byte[] output = null;
        ByteArrayInputStream bais = null;
        ByteArrayOutputStream baos = null;
        GZIPOutputStream gos = null;
        try {
            bais = new ByteArrayInputStream(data);
            baos = new ByteArrayOutputStream();
            gos = new GZIPOutputStream(baos);
            int count;
            byte[] buf = new byte[BUFFER];
            while ((count = bais.read(buf, 0, BUFFER)) != -1) {
                gos.write(buf, 0, count);
            }
            gos.finish();
            gos.flush();

            output = baos.toByteArray();
        } finally {
            IOUtils.closeQuietly(bais);
            IOUtils.closeQuietly(gos);
            IOUtils.closeQuietly(baos);
        }

        return output;
    }

    /**
     * 解压
     *
     * @param data
     * @return
     * @throws java.io.IOException
     */
    public static byte[] decompress(byte[] data) throws IOException {
        ByteArrayInputStream bais = null;
        ByteArrayOutputStream baos = null;
        GZIPInputStream gis = null;
        byte[] result = null;
        try {
            bais = new ByteArrayInputStream(data);
            baos = new ByteArrayOutputStream();
            gis = new GZIPInputStream(bais);
            int count;
            byte buf[] = new byte[BUFFER];
            while ((count = gis.read(buf, 0, BUFFER)) != -1) {
                baos.write(buf, 0, count);
            }
            result = baos.toByteArray();
        } finally {
            IOUtils.closeQuietly(gis);
            IOUtils.closeQuietly(baos);
            IOUtils.closeQuietly(bais);
        }
        return result;
    }
}
