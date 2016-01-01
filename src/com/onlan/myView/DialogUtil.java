package com.onlan.myView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.onlan.lazymemo.APPConfigure;
import com.onlan.lazymemo.R;

public class DialogUtil extends Dialog implements
		android.view.View.OnClickListener {
	private TextView tv;
	private String content = null;

	public DialogUtil(Context context, String content) {
		super(context, R.style.MyDialog);
		// TODO Auto-generated constructor stub
		this.content = content;
		this.setCanceledOnTouchOutside(false);
		this.setCancelable(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialogutil_layout);
		tv = (TextView) findViewById(R.id.dialog_tv);
		Button Btn = (Button) findViewById(R.id.dialog_btn);
		if (content != null && content.trim().length() > 0) {
			tv.setText(Html.fromHtml(content));
		} else {
			tv.setText(Html.fromHtml(APPConfigure.DialogTextShow));
		}
		Btn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		this.dismiss();

	}

}