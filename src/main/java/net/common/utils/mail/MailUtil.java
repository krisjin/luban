package net.common.utils.mail;


import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Date;
import java.util.Properties;

/**
 * @author krisjin (mailto:krisjin86@163.com)
 * @date 2014-5-23下午4:16:22
 */

public class MailUtil {

    public static void sendTextMail(MailInfo info) throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.host", info.getMailServerHost());
        props.put("mail.smtp.auth", info.isAuth());
        Authenticator auth = null;
        if (info.isAuth()) {
            auth = new EmailAuthenticator(info.getUsername(), info.getPassword());
        }

        Session session = Session.getDefaultInstance(props, auth);

        MimeMessage message = new MimeMessage(session);
        message.setSentDate(new Date());
        Address address = new InternetAddress(info.getMailFrom(), info.getMailName());
        message.setFrom(address);

        String mailTo = info.getMailTo();
        String[] mailsTo = mailTo.split(",");

        InternetAddress[] mailToAddress = new InternetAddress[mailsTo.length];
        for (int i = 0; i < mailsTo.length; i++) {
            mailToAddress[i] = new InternetAddress(mailsTo[i]);
        }
        message.setRecipients(Message.RecipientType.TO, mailToAddress);
        message.setSubject(info.getMailSubject());
        message.setText(info.getBody());
        Transport.send(message);
    }

    public static void sendHtmlEmail(MailInfo info) throws Exception {

        Properties props = new Properties();
        props.put("mail.smtp.host", info.getMailServerHost());
        props.put("mail.smtp.auth", info.isAuth());

        Authenticator auth = null;
        if (info.isAuth()) {
            auth = new EmailAuthenticator(info.getUsername(), info.getPassword());
        }
        Session session = Session.getDefaultInstance(props, auth);

        Multipart multipart = new MimeMultipart();
        BodyPart html = new MimeBodyPart();

        MimeMessage message = new MimeMessage(session);
        message.setSentDate(new Date());
        Address address = new InternetAddress(info.getMailFrom(), info.getUsername());
        message.setFrom(address);

        String mailTo = info.getMailTo();
        String[] mailsTo = mailTo.split(",");
        InternetAddress[] mailToAddress = new InternetAddress[mailsTo.length];
        for (int i = 0; i < mailsTo.length; i++) {
            mailToAddress[i] = new InternetAddress(mailsTo[i]);
        }
        message.setRecipients(Message.RecipientType.TO, mailToAddress);
        message.setSubject(info.getMailSubject());
        html.setContent(info.getHtmlBody(), "text/html; charset=utf-8");

        multipart.addBodyPart(html);
        message.setContent(multipart);
        Transport.send(message);
    }

    public static void sendAttachEmail(MailInfo info, String[] filePath) throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.host", info.getMailServerHost());
        props.put("mail.smtp.auth", info.isAuth());

        Authenticator auth = null;
        if (info.isAuth()) {
            auth = new EmailAuthenticator(info.getUsername(), info.getPassword());
        }
        Session session = Session.getDefaultInstance(props, auth);
        Message msg = new MimeMessage(session);
        msg.setSentDate(new Date());
        msg.setFrom(new InternetAddress(info.getMailFrom(), info.getUsername()));
        msg.setSubject(info.getMailSubject());

        String[] mailsTo = info.getMailTo().split(",");
        InternetAddress[] mailToAddress = new InternetAddress[mailsTo.length];
        for (int i = 0; i < mailsTo.length; i++) {
            mailToAddress[i] = new InternetAddress(mailsTo[i]);
        }
        msg.setRecipients(Message.RecipientType.TO, mailToAddress);
        Multipart multipart = new MimeMultipart();
        BodyPart body = new MimeBodyPart();
        body.setContent(info.getHtmlBody(), "text/html; charset=utf-8");
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

}
