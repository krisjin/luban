package net.common.utils;

import net.common.utils.mail.MailInfo;
import net.common.utils.mail.MailUtil;

public class MailTest {

    public static void main(String[] args) throws Exception {
        MailInfo info = new MailInfo();

        info.setHtmlBody("<div style='font-size:18px;'>Hellowrod</div>");
        info.setMailFrom("krisibm@163.com");
        info.setMailTo("jinguishi@tianler.com");
        info.setMailSubject("带人的艺术与-Kindle版");
        info.setUsername("krisibm@163.com");
        info.setPassword("");
        info.setMailServerHost("smtp.163.com");
        info.setIsAuth(true);
        info.setMailName("NIHO,中午好!");
        MailUtil.sendAttachEmail(info, new String[]{"e:/带人的艺术与.mobi"});
    }

}
