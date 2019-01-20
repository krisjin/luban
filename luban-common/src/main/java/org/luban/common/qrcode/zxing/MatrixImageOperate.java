package org.luban.common.qrcode.zxing;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Hashtable;


public class MatrixImageOperate {
    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;
    private static final String DEFAULT_IMAGE_FORMAT = "png";
    private static final int QR_CODE_WIDTH = 300;
    private static final int QR_CODE_HEIGHT = 300;
    private static final Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();

    private MatrixImageOperate() {
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
    }



    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    public static boolean writeToFile(String content, File outputFile) throws IOException, WriterException {
        BitMatrix bitMatrix = getBitMatrix(content);
        BufferedImage image = toBufferedImage(bitMatrix);
        boolean isSuccess = ImageIO.write(image, DEFAULT_IMAGE_FORMAT, outputFile);
        return isSuccess;
    }

    public static boolean writeToOutputStream(String content, OutputStream stream) throws IOException, WriterException {
        BitMatrix matrix = getBitMatrix(content);
        BufferedImage image = toBufferedImage(matrix);
        boolean isSuccess = ImageIO.write(image, DEFAULT_IMAGE_FORMAT, stream);
        return isSuccess;
    }

    public static ByteArrayInputStream writeToInputStream(String content) throws IOException, WriterException {
        BitMatrix matrix = getBitMatrix(content);
        BufferedImage image = toBufferedImage(matrix);
        final ByteArrayOutputStream output = new ByteArrayOutputStream() {
            @Override
            public synchronized byte[] toByteArray() {
                return this.buf;
            }
        };
        ImageIO.write(image, DEFAULT_IMAGE_FORMAT, output);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(output.toByteArray(), 0, output.size());
        return byteArrayInputStream;
    }


    private static BitMatrix getBitMatrix(String content) throws WriterException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QR_CODE_WIDTH, QR_CODE_HEIGHT, hints);
        return bitMatrix;
    }

    private void setEncodeTypeHits(Hashtable<EncodeHintType, String> hits) {
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
    }

}
