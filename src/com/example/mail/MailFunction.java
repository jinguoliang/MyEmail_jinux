package com.example.mail;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.util.Log;

public class MailFunction {

	String from = "张三";// 发送者,显示的发件人名字


	private Account mAccount;


	private static String TAG = "MailFunction";
	
	public MailFunction() {
		mAccount = Account.getAccount();
	}

	static public void send(String[] recipients, String subject, String body,Account account)
			throws Exception {

		try {
			
			Session s=account.getOutSession();
			MimeMessage msg = new MimeMessage(s);
			Log.e(TAG,s.toString());
			//set the info of email
			msg.setSentDate(new Date());
			
			InternetAddress fromAddress = new InternetAddress(account.getUser(), account.getUser(),
					"UTF-8");
			msg.setFrom(fromAddress);
			
			InternetAddress[] toAddress = new InternetAddress[recipients.length];
			for (int i = 0; i < recipients.length; i++){
				toAddress[i] = new InternetAddress(recipients[i]);
				Log.e(TAG,recipients[i]);
			}
			msg.setRecipients(Message.RecipientType.TO, toAddress);
			
			msg.setSubject(subject, "UTF-8");
			msg.setText(body, "UTF-8");
			msg.saveChanges();
			Log.e(TAG,"sending");
			Transport transport=account.getTransport();
			if(!transport.isConnected())
				transport.connect();
			transport.sendMessage(msg, msg.getAllRecipients());
			Log.e(TAG,"send ok");
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}


}
