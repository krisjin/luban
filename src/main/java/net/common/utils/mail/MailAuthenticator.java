package net.common.utils.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @author shijingui
 * @date 2014-5-23下午4:31:37
 */

public class MailAuthenticator extends Authenticator {

    private String userName;
    private String password;

    public MailAuthenticator() {
        super();
    }

    public MailAuthenticator(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(this.userName, this.password);
    }

}
