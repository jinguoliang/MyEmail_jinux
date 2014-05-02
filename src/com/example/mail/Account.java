package com.example.mail;

import java.io.Serializable;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.URLName;

import android.util.Log;

public class Account implements Serializable {
	private static final String TAG = Account.class.getName();

	private String mUser = "jinguol999@163.com";

	private String mPassword = "jin8146302";
	private int mPort = 25;
	private String mServer = "smtp.163.com";// 邮件服务器mail.cpip.net.cn

	static private Account mAccount;
	private Session mInSession;
	private Transport mTransport;

	private Store mStore;

	private Session mOutSession;


	public String getUser() {
		return mUser;
	}

	public void setUser(String user) {
		this.mUser = user;
	}

	public String getPassword() {
		return mPassword;
	}

	public void setPassword(String password) {
		this.mPassword = password;
	}

	public int getPort() {
		return mPort;
	}

	public void setPort(int port) {
		mPort = port;
	}

	public String getServer() {
		return mServer;
	}

	public void setServer(String server) {
		this.mServer = server;
	}

	// public Account(String mUser, String mPassword, int mPort, String mServer)
	// {
	// super();
	// this.mUser = mUser;
	// this.mPassword = mPassword;
	// this.mPort = mPort;
	// this.mServer = mServer;
	// }
	private Account() {

	}

	public static Account getAccount() {
		if (mAccount == null) {
			return new Account();
		} else {
			return mAccount;
		}
	}

	public Transport getTransport() throws Exception {
		if (mTransport == null) {
			throw new Exception("no login");
		} else
			return mTransport;
	}

	public Session connect() throws MessagingException {

		return mInSession;
	}

	private Properties getProperties() {
		Properties props = new Properties();
		props.put("mail.smtp.host", mServer);
		props.put("mail.smtp.port", String.valueOf(mPort));
		props.put("mail.smtp.auth", "true");
		return props;
	}

	private String getInBoxServer(String address) {

		return "smtp.163.com";
	}

	private Session getSession() {
		return Session.getDefaultInstance(getProperties(), null);
	}
	
	public Session getInSession() {
		if (mInSession == null)
			mInSession = getSession();
		return mInSession;
	}
	

	public Session getOutSession(){
		if (mOutSession == null)
			mOutSession = getSession();
		return mOutSession;
	}

	public void loginOutBox(String address, String pass) throws Exception {
		mUser = address;
		mServer = getInBoxServer(address);

		Log.e(TAG, "get transport");
		mTransport = getOutSession().getTransport("smtp");
		Log.e(TAG, "connecting");
		mTransport.connect(mServer, mUser, mPassword);
		Log.e(TAG, "connected");
	}

	public void loginInBox(String address, String pass) throws Exception {
		Transport transport = null;

		mUser = address;
		mServer = getInBoxServer(address);

		URLName urln = new URLName("pop3", getOutBoxServer(address), 110, null,
				address, pass);
		mStore = getInSession().getStore(urln);
		Log.e(TAG, "connecting");
		mStore.connect();
		Log.e(TAG, "connected");
	}

	private String getOutBoxServer(String address) {

		return "pop3.163.com";
	}

}
