package org.bscl.common.qrcode.zxing;

import com.google.zxing.WriterException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * User: shijingui
 * Date: 2016/5/31
 */
public class QRCodeHelp {

    /**
     * 指定文件方式生成二维码
     *
     * @param content 二维码内容
     * @param file    二维码输出文件
     * @return
     * @throws IOException
     * @throws WriterException
     */
    public static boolean generate(String content, File file) throws IOException, WriterException {
        return MatrixToImageOperate.writeToFile(content, file);
    }

    /**
     * 输出流的方式生成二维码
     *
     * @param content      二维码内容
     * @param outputStream 二维码输出流
     * @return
     * @throws IOException
     * @throws WriterException
     */
    public static boolean generate(String content, OutputStream outputStream) throws IOException, WriterException {
        return MatrixToImageOperate.writeToOutputStream(content, outputStream);
    }

    /**
     * 生成的二维码图片转换成输入流
     *
     * @param content
     * @return InputStream 输入流
     * @throws IOException
     * @throws WriterException
     */
    public static InputStream generate(String content) throws IOException, WriterException {
        return MatrixToImageOperate.writeToInputStream(content);
    }


}
