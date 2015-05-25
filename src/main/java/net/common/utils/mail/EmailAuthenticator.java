package net.common.utils.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @author krisjin (mailto:krisjin86@163.com)
 * @date 2014-5-23下午4:31:37
 */

public class EmailAuthenticator extends Authenticator {
	
	private String userName;
	private String password;

	public EmailAuthenticator() {
		super();
	}

	public EmailAuthenticator(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	protected PasswordAuthentication getPasswordAuthentication() {

		return new PasswordAuthentication(this.userName, this.password);
	}

}
