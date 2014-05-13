package com.example.mail;

import com.example.mail.Utils.WaitDialog;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MailEdit extends Activity {

	private static final int MSG_EDIT_SEND = 1;
	protected static final int MSG_EDIT_SEND_SUCCESS = 1;
	protected static final int MSG_EDIT_SEND_FAILED = -1;
	protected static final String TAG = "MailEdit";
	private EditText recipients;
	private EditText title;
	private EditText content;
	private Button contact_btn;
	private Button attach_btn;

	Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_EDIT_SEND_SUCCESS:
				Utils.showToast(MailEdit.this, "send success");

				break;
			case MSG_EDIT_SEND_FAILED:
				Utils.showToast(MailEdit.this, "send failed");
				break;
			default:
				break;
			}
		};
	};

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

			// new Thread() {
			// public void run() {

			// };
			// }.start();

			new WaitDialog(this, "正在发送...", MSG_EDIT_SEND, mhandler)
					.execute(new Utils.MyRun[] { new Utils.MyRun() {

						@Override
						public int run() {
							MailApplication application = (MailApplication) getApplication();
							try {
								MailFunction.send(new String[] { recipients
										.getText().toString() }, title
										.getText().toString(), content
										.getText().toString(),
										application.account);
							} catch (Exception e) {
								Log.e(TAG,Log.getStackTraceString(e));
								return -1;

							}

							return 1;
						}
					} });
			break;

		default:
			break;
		}
	}
}
