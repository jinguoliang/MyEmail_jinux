package com.example.mail;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MailContentActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mail_containt);
		
		CheckBox showDetail=(CheckBox) findViewById(R.id.expand_cb);
		final LinearLayout mailDetail=(LinearLayout) findViewById(R.id.mail_detail);

		showDetail.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(arg1){
					mailDetail.setVisibility(View.VISIBLE);
				}else{
					mailDetail.setVisibility(View.GONE);
				}
			}
		});
	}
}
