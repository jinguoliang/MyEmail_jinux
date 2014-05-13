package com.example.mail;

import java.net.URL;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Utils {
	public static String splitOutAddress(String addr) {
		int start = addr.indexOf('<');
		int end = addr.indexOf('>');
		return addr.substring(start + 1, end);
	}

	public static String splitOutUser(String addr) {
		addr = splitOutAddress(addr);
		int end = addr.indexOf('@');
		return addr.substring(0, end);
	}

	public static boolean requiredFieldValid(TextView view) {
		return view.getText() != null && view.getText().length() > 0;
	}

	public static boolean isValideEmail(String email) {
		return email.indexOf('@') > 0;
	}

	public static String getText(EditText v) {
		return v.getText().toString();
	}

	static public class WaitDialog extends AsyncTask<MyRun, Integer, Long> {

		private ProgressDialog mRoll;
		private Handler mHandler;
		private int mWhich;

		public WaitDialog(Context c, String title, int which, Handler mHandler) {
			mRoll = new ProgressDialog(c);
			mRoll.setTitle(title);
			mRoll.setCanceledOnTouchOutside(false);
			mRoll.show();
			this.mHandler = mHandler;
			this.mWhich = which;
		}
		
		

		protected Long doInBackground(MyRun... run) {
			// int count = urls.length;
			// long totalSize = 0;
			// for (int i = 0; i < count; i++) {
			// totalSize += Downloader.downloadFile(urls[i]);
			// publishProgress((int) ((i / (float) count) * 100));
			// // Escape early if cancel() is called
			// if (isCancelled()) break;
			// }

			return (long) run[0].run();
		}

		protected void onProgressUpdate(Integer... progress) {
		}

		protected void onPostExecute(Long result) {
			mHandler.sendEmptyMessage((int) (mWhich * result));
			mRoll.dismiss();
		}
	}

	static public interface MyRun {
		int run();
	}

	public static void showToast(Context c,String string) {
		Toast.makeText(c, string, Toast.LENGTH_LONG).show();;
	}

	public static void setGlobalAccount(Context c,Account mAccount) {
		((MailApplication)c.getApplicationContext()).account=mAccount;
		
	}

	public static Account getGlobalAccount(Context c) {
		return ((MailApplication)c.getApplicationContext()).account;
	}
}
