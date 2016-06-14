package org.bscl.common.mail;


import com.google.common.base.Strings;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Date;
import java.util.Properties;

/**
 * 邮件工具类
 *
 * @author shijingui
 * @date 2014-5-23下午4:16:22
 */

public final class MailUtil {

    private static final String MAIL_HOST_PROP = "mail.smtp.host";
    private static final String MAIL_AUTH_PROP = "mail.smtp.auth";

    /**
     * 发送文本邮件
     *
     * @param info
     * @throws Exception
     */
    public static void sendTextMail(MailInfo info) throws Exception {
        Properties props = new Properties();
        props.put(MAIL_HOST_PROP, info.getServerHost());
        props.put(MAIL_AUTH_PROP, info.isAuth());
        Authenticator auth = getAuthenticator(info);

        Session session = Session.getDefaultInstance(props, auth);

        MimeMessage message = new MimeMessage(session);
        message.setSentDate(new Date());
        Address address = new InternetAddress(info.getFrom(), info.getMailName());
        message.setFrom(address);

        String mailTo = info.getTo();
        String[] mailsTo = mailTo.split(",");

        InternetAddress[] mailToAddress = new InternetAddress[mailsTo.length];
        for (int i = 0; i < mailsTo.length; i++) {
            mailToAddress[i] = new InternetAddress(mailsTo[i]);
        }
        message.setRecipients(Message.RecipientType.TO, mailToAddress);
        message.setSubject(info.getSubject());
        message.setText(info.getBody());
        Transport.send(message);
    }

    /**
     * 发送HTML格式邮件
     *
     * @param info
     * @throws Exception
     */
    public static void sendHtmlEmail(MailInfo info) throws Exception {

        Properties props = new Properties();
        props.put(MAIL_HOST_PROP, info.getServerHost());
        props.put(MAIL_AUTH_PROP, info.isAuth());

        Authenticator auth = getAuthenticator(info);
        Session session = Session.getDefaultInstance(props, auth);

        Multipart multipart = new MimeMultipart();
        BodyPart html = new MimeBodyPart();

        MimeMessage message = new MimeMessage(session);
        message.setSentDate(new Date());
        Address address = new InternetAddress(info.getFrom(), info.getMailName());
        message.setFrom(address);

        String mailTo = info.getTo();
        String[] mailsTo = mailTo.split(",");
        InternetAddress[] mailToAddress = new InternetAddress[mailsTo.length];
        for (int i = 0; i < mailsTo.length; i++) {
            mailToAddress[i] = new InternetAddress(mailsTo[i]);
        }
        message.setRecipients(Message.RecipientType.TO, mailToAddress);
        message.setSubject(info.getSubject());
        html.setContent(info.getBody(), "text/html; charset=utf-8");

        multipart.addBodyPart(html);
        message.setContent(multipart);
        Transport.send(message);
    }

    /**
     * 发送带附件的邮件
     *
     * @param info
     * @param filePath
     * @throws Exception
     */
    public static void sendAttachEmail(MailInfo info, String[] filePath) throws Exception {
        Properties props = new Properties();
        props.put(MAIL_HOST_PROP, info.getServerHost());
        props.put(MAIL_AUTH_PROP, info.isAuth());

        Authenticator auth = null;
        if (info.isAuth()) {
            auth = new MailAuthenticator(info.getUsername(), info.getPassword());
        }
        Session session = Session.getDefaultInstance(props, auth);
        Message msg = new MimeMessage(session);
        msg.setSentDate(new Date());
        msg.setFrom(new InternetAddress(info.getFrom(), info.getUsername()));
        msg.setSubject(info.getSubject());

        String[] mailsTo = info.getTo().split(",");
        InternetAddress[] mailToAddress = new InternetAddress[mailsTo.length];
        for (int i = 0; i < mailsTo.length; i++) {
            mailToAddress[i] = new InternetAddress(mailsTo[i]);
        }
        msg.setRecipients(Message.RecipientType.TO, mailToAddress);
        Multipart multipart = new MimeMultipart();
        BodyPart body = new MimeBodyPart();
        body.setContent(info.getBody(), "text/html; charset=utf-8");
        multipart.addBodyPart(body);

        for (int i = 0; i < filePath.length; i++) {
            BodyPart bodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filePath[i]);
            bodyPart.setDataHandler(new DataHandler(source));

            String fileName = MimeUtility.encodeWord(filePath[i].substring(filePath[i].lastIndexOf("/") + 1));

            // if(fileName.lastIndexOf(".") !=-1){
            // fileName=fileName.substring(0, fileName.lastIndexOf("."));
            // }
            bodyPart.setFileName(fileName);
            multipart.addBodyPart(bodyPart);

        }
        msg.setContent(multipart);
        Transport.send(msg);
    }

    private static Authenticator getAuthenticator(MailInfo mailInfo) {
        if (mailInfo == null)
            throw new NullPointerException("Mail info is null");
        if (Strings.isNullOrEmpty(mailInfo.getUsername()) || Strings.isNullOrEmpty(mailInfo.getPassword()))
            throw new IllegalArgumentException("Mail username or password is null");

        Authenticator authenticator = new MailAuthenticator(mailInfo.getUsername(), mailInfo.getPassword());
        return authenticator;
    }
}
