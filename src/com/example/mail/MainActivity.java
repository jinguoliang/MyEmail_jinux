package com.example.mail;

import java.util.ArrayList;
import java.util.HashMap;

import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity {

	final static String[] EmailListItemStrs = new String[] { "name", "subject",
			"date", "stared" };
	protected static final String TAG = MainActivity.class.getName();
	private static final String EXTRA_ACCOUNT = "extra_account";
	private MyEmailListAdapter mListAdapter;
	private ListView mlv;
	private Account mAccount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater li = getLayoutInflater();
		LinearLayout leftPane = (LinearLayout) li.inflate(R.layout.left_pane,
				null);
		RelativeLayout mainView = (RelativeLayout) li.inflate(
				R.layout.mail_list, null);
		SlidingPaneLayout slide = new SlidingPaneLayout(this);
		slide.setParallaxDistance(80);
		slide.addView(leftPane);
		slide.addView(mainView);

		setContentView(slide);

		mlv = (ListView) findViewById(R.id.mail_list);

		mlv.setAdapter((mListAdapter = new MyEmailListAdapter(this,
				new ArrayList<HashMap<String, Object>>())));

		
		mAccount = Utils.getGlobalAccount(this);
		new Thread() {
		public void run() {
			try {
				MailFunction.send(new String[] { "jinguol999@163.com" },
						"hello","test",mAccount);
			} catch (Exception e) {
				Log.e(TAG,"error");
			}
		};
	}.start();
	}

	class MyEmailListAdapter extends SimpleAdapter {

		ArrayList<HashMap<String, Object>> mData;

		public MyEmailListAdapter(Context context,
				ArrayList<HashMap<String, Object>> data) {
			super(context, (data), R.layout.mail_list_item, EmailListItemStrs,
					new int[] { R.id.from_name, R.id.subject, R.id.from_date,
							R.id.is_stared });
			mData = data;
		}

		public void updateData(ArrayList<HashMap<String, Object>> data) {
			mData.clear();
			mData.addAll(data);
			this.notifyDataSetInvalidated();
		}

		public void addData(HashMap<String, Object> data) {
			mData.add(data);
			runOnUiThread(new Runnable() {
				public void run() {
					notifyDataSetChanged();
					mlv.invalidate();
				}
			});

		}

	}

	public void onClick1(View v) {
		switch (v.getId()) {
		case R.id.newmail:
			Log.e("mail", "new mail");
			Intent i = new Intent(this, MailEdit.class);
			startActivity(i);
			break;
		case R.id.receive_box_btn:
			new Thread() {
				public void run() {
					try {
						Session session = ReceiveMail.getSession();
						Store store = ReceiveMail.getStore();
						store.connect();
						Folder folder = ReceiveMail.getFolder("INBOX");
						ReceiveMail.listMessageInFolder(folder, mListAdapter);
						store.close();
					} catch (Exception e) {
						Log.getStackTraceString(e);
						Log.e(TAG, "error");
					}

				};
			}.start();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public static void actionOpenMain(Context c, Account mAccount) {
		Utils.setGlobalAccount(c,mAccount);
		Intent i = new Intent(c, MainActivity.class);
		c.startActivity(i);
	}

}
