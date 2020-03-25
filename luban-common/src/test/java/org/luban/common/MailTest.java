package org.luban.common;

import org.luban.common.mail.MailInfo;
import org.luban.common.mail.MailUtil;
import org.junit.Test;
import org.luban.common.mail.MailInfo;
import org.luban.common.mail.MailUtil;

public class MailTest {

    @Test
    public void sendMail() throws Exception {
        MailInfo info = new MailInfo();
        String nick=javax.mail.internet.MimeUtility.encodeText("dd");
        info.setBody("<h1 style='color:red;'>Helloddddddddaaaaaaaaaaaaaddddddddddddddddfsaxxx World!!!!!!!!!!!!!!</h1>");
        info.setFrom(nick+"<masksalt@163.com>");
        info.setTo("krisibm@163.com");
        info.setSubject("Hello World!");
        info.setUsername("masksalt@163.com");
        info.setPassword("krisjin2016");
        info.setServerHost("smtp.163.com");
        info.setIsAuth(true);
        info.setMailName("Hello Worlddddddddddddddddddddddddddddd!!");
        MailUtil.sendHtmlEmail(info);
    }

}
