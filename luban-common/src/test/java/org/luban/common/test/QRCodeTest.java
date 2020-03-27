package org.luban.common.test;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import org.luban.common.qrcode.zxing.QRCodeHelp;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

/**
 * User: krisjin
 * Date: 2016/5/24
 */
public class QRCodeTest {

    @Test
    public void test() throws IOException, WriterException {

        String text = "http://sale.jd.com/act/JrwqAk8oGmWQCsB.html?cpdad=1DLSUE"; // 二维码内容
        int width = 300; // 二维码图片宽度
        int height = 300; // 二维码图片高度
        String format = "png";// 二维码的图片格式

        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");   // 内容所使用字符集编码

        BitMatrix bitMatrix = null;


        File outputFile = new File("d:" + File.separator + "url2222222222.jpg");
        QRCodeHelp.generate(text, outputFile);
    }
}
