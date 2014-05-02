package com.example.mail;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MailEdit extends Activity {
	private EditText recipients;
	private EditText title;
	private EditText content;
	private Button contact_btn;
	private Button attach_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mail_edit);
		recipients = (EditText) findViewById(R.id.recipients);
		title = (EditText) findViewById(R.id.title);
		content = (EditText) findViewById(R.id.content);

		contact_btn = (Button) findViewById(R.id.contact);
		attach_btn = (Button) findViewById(R.id.attach);

	}

	public void onButtonClick(View v) {
		switch (v.getId()) {
		case R.id.send_btn:

//			new Thread() {
//				public void run() {
//					MailFunction mf = new MailFunction();
//					try {
//						mf.send(new String[] { recipients.getText().toString() },
//								title.getText().toString(), content.getText()
//										.toString());
//					} catch (Exception e) {
//					}
//				};
//			}.start();
			break;

		default:
			break;
		}
	}
}
