package com.example.mail;

import com.example.mail.Utils.WaitDialog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener,
		TextWatcher {

	private EditText mEmailView;
	private EditText mPasswordView;
	private Button mNextButton;
	private Button mManualSetupButton;
	private CheckBox mShowPasswordView;

	SharedPreferences mSp;
	String ADDRESS_PREF = "email_address";
	String PASSWORD_PREF = "password";
	private CheckBox mAutoLogin;
	private Account mAccount;

	final static int MSG_INBOX_SUCCESS = 1;
	final static int MSG_INBOX_FAILED = -1;
	final static int MSG_OUTBOX_SUCCESS = 2;
	final static int MSG_OUTBOX_FAILED = -2;

	final static int MSG_LOGIN_INBOX = 1;
	final static int MSG_LOGIN_OUTBOX = 2;

	Handler mHandler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_INBOX_SUCCESS:
				Utils.showToast(LoginActivity.this, "登录收件箱成功"); // 先登录收件箱，再登录发件箱
				loginOutBox();
				break;
			case MSG_INBOX_FAILED:
				Utils.showToast(LoginActivity.this, "登录收件箱失败");
				break;
			case MSG_OUTBOX_SUCCESS:
				Utils.showToast(LoginActivity.this, "登录发件箱成功");// 登录发件箱成功则算是登录成功了
				loginSuccessfull();

				break;
			case MSG_OUTBOX_FAILED:
				Utils.showToast(LoginActivity.this, "登录发件箱失败");

				break;

			default:
				break;
			}
		}

	};

	final private String MAIL_ADDR = "mail_addr";
	final private String MAIL_PASS = "mail_pass";
	String MAIL_IS_AUTOLOGIN = "auto_login";

	private void loginSuccessfull() {

		saveConfig(Utils.getText(mEmailView), Utils.getText(mPasswordView));
		MainActivity.actionOpenMain(LoginActivity.this, mAccount);
		LoginActivity.this.finish();
	}

	private void saveConfig(String user, String pass) {
		SharedPreferences.Editor editor = mSp.edit();
		editor.putBoolean(MAIL_IS_AUTOLOGIN, mAutoLogin.isChecked());
		if (mAutoLogin.isChecked() == true) {
			editor.putString(MAIL_ADDR, user);
			editor.putString(MAIL_PASS, pass);
		}

		editor.commit();
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		mEmailView = (EditText) findViewById(R.id.account_email);
		mPasswordView = (EditText) findViewById(R.id.account_password);
		mNextButton = (Button) findViewById(R.id.next);
		mManualSetupButton = (Button) findViewById(R.id.manual_setup);

		mNextButton.setOnClickListener(this);
		mManualSetupButton.setOnClickListener(this);

		mEmailView.addTextChangedListener(this);
		mPasswordView.addTextChangedListener(this);

		mShowPasswordView = (CheckBox) findViewById(R.id.account_show_password);
		mAutoLogin = (CheckBox) findViewById(R.id.account_auto_login);
		CheckBox.OnCheckedChangeListener checkboxListener = new CheckBox.OnCheckedChangeListener() {
			private boolean mIsAutoLogin;

			@Override
			public void onCheckedChanged(CompoundButton v, boolean isChecked) {
				switch (v.getId()) {
				case R.id.account_show_password:
					if (isChecked) {
						mPasswordView
								.setInputType(InputType.TYPE_CLASS_TEXT
										| InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
					} else {
						mPasswordView.setInputType(InputType.TYPE_CLASS_TEXT
								| InputType.TYPE_TEXT_VARIATION_PASSWORD);
					}
					Editable etable = mPasswordView.getText();
					Selection.setSelection(etable, etable.length());
					mPasswordView.setTypeface(mEmailView.getTypeface());
					break;
				case R.id.account_auto_login:
					mIsAutoLogin = isChecked;
					break;

				default:
					break;
				}

			}
		};
		mShowPasswordView.setOnCheckedChangeListener(checkboxListener);
		mAutoLogin.setOnCheckedChangeListener(checkboxListener);

		mSp = getPreferences(Context.MODE_PRIVATE);
		
		mAutoLogin.setChecked(mSp.getBoolean(MAIL_IS_AUTOLOGIN, false));
		if (mSp.getString(MAIL_ADDR, "") != "") {
			mEmailView.setText(mSp.getString(MAIL_ADDR, ""));
			mPasswordView.setText(mSp.getString(MAIL_PASS, ""));
			onNext();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.next:
			onNext();

			break;

		case R.id.manual_setup:
			Toast.makeText(this, "The function is not implement now",
					Toast.LENGTH_LONG);
			break;

		default:
			break;
		}
	}

	private void onNext() {
		mAccount = Account.getAccount();

		loginInBox();
	}

	private void loginOutBox() {
		new WaitDialog(this, "登录收件箱", MSG_LOGIN_OUTBOX, mHandler)
				.execute(new Utils.MyRun[] { new Utils.MyRun() {

					@Override
					public int run() {

						try {
							mAccount.loginOutBox(Utils.getText(mEmailView),
									Utils.getText(mPasswordView));
						} catch (Exception e) {
							e.printStackTrace();
							return -1;
						}
						return 1;
					}
				} });
	}

	private void loginInBox() {
		Utils.WaitDialog waiting = new WaitDialog(this, "登录发件箱",
				MSG_LOGIN_INBOX, mHandler);
		waiting.execute(new Utils.MyRun[] { new Utils.MyRun() {

			@Override
			public int run() {
				try {
					mAccount.loginOutBox(Utils.getText(mEmailView),
							Utils.getText(mPasswordView));
				} catch (Exception e) {
					e.printStackTrace();
					return -1;
				}
				return 1;
			}
		} });
	}

	private void validateFields() {
		boolean valid = Utils.requiredFieldValid(mEmailView)
				&& Utils.requiredFieldValid(mPasswordView)
				&& Utils.isValideEmail(mEmailView.getText().toString());
		mNextButton.setEnabled(valid);
		mManualSetupButton.setEnabled(valid);
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		validateFields();
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

}
