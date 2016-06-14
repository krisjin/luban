package org.bscl.common;

import org.bscl.common.mail.MailInfo;
import org.bscl.common.mail.MailUtil;
import org.junit.Test;

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
        info.setPassword("shijingui2016");
        info.setServerHost("smtp.163.com");
        info.setIsAuth(true);
        info.setMailName("Hello Worlddddddddddddddddddddddddddddd!!");
        MailUtil.sendHtmlEmail(info);
    }

}
