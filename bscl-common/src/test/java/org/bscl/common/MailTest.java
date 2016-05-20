package org.bscl.common;

import org.bscl.common.mail.MailInfo;
import org.bscl.common.mail.MailUtil;
import org.junit.Test;

public class MailTest {

    @Test
    public void sendMail() throws Exception {
        MailInfo info = new MailInfo();

        info.setBody("<h1 style='color:red;'>Hello World!!!!!!!!!!!!!!</h1>");
        info.setFrom("");
        info.setTo("");
        info.setSubject("Hello World!");
        info.setUsername("");
        info.setPassword("");
        info.setServerHost("smtp.163.com");
        info.setIsAuth(true);
        info.setMailName("Hello World!!");
        MailUtil.sendHtmlEmail(info);
    }

}
