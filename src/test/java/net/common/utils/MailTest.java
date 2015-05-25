package net.common.utils;

import net.common.utils.mail.MailInfo;
import net.common.utils.mail.MailUtil;

public class MailTest {
	
	public static void main(String[] args) throws Exception{
		MailInfo info =new MailInfo();
		
		info.setHtmlBody("<div style='font-size:18px;'>Hellowrod</div>");
		info.setMailFrom("krisibm@163.com");
		info.setMailTo("shijingui@staff.hexun.com");
		info.setMailSubject("2015");
		info.setUsername("krisibm@163.com");
		info.setPassword("");
		info.setMailServerHost("smtp.163.com");
		MailUtil.sendAttachEmail(info, new String[]{"d:/daocache.rar"});
	}

}
