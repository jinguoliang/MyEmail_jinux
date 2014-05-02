package com.example.mail;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MailService extends Service {

	final static int CMD_LOGIN_INBOX=1;
	final static int CMD_LOGIN_OUTBOX=2;
//	final static int MSG_LOGIN_INBOX=1;
//	final static int MSG_LOGIN_INBOX=1;
//	final static int MSG_LOGIN_INBOX=1;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		switch (startId) {
		case CMD_LOGIN_INBOX:
			
			break;
		case CMD_LOGIN_OUTBOX:
			
			break;

		default:
			break;
		}
		return super.onStartCommand(intent, flags, startId);
	}

}
