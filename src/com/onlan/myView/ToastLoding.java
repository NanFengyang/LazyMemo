package com.onlan.myView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.onlan.lazymemo.APPConfigure;
import com.onlan.lazymemo.R;

public class ToastLoding extends Dialog implements
		android.view.View.OnClickListener {
	private TextView tv;
	private String content = null;

	public ToastLoding(Context context, String content) {
		super(context, R.style.MyDialog);
		// TODO Auto-generated constructor stub
		this.content = content;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialogutil_layout);
		tv = (TextView) findViewById(R.id.dialog_tv);
		Button Btn = (Button) findViewById(R.id.dialog_btn);
		if (content == null && content.trim().length() <= 0) {

			content = APPConfigure.DialogTextShow;
		}
		tv.setText(Html.fromHtml(content));
		Btn.setVisibility(View.GONE);
		onstartHandler.sendEmptyMessageDelayed(NEXTPAGE1, 300);
	}

	private final int NEXTPAGE1 = 1;
	private final int NEXTPAGE2 = 2;
	private final int NEXTPAGE3 = 3;

	private Handler onstartHandler = new Handler() {
		@Override
		public void handleMessage(Message msghand) {
			switch (msghand.what) {
			case NEXTPAGE1:
				tv.setText(Html.fromHtml(content + "."));
				onstartHandler.sendEmptyMessageDelayed(NEXTPAGE2, 300);
				break;
			case NEXTPAGE2:
				tv.setText(Html.fromHtml(content + ".."));
				onstartHandler.sendEmptyMessageDelayed(NEXTPAGE3, 300);
				break;
			case NEXTPAGE3:
				tv.setText(Html.fromHtml(content + "..."));
				onstartHandler.sendEmptyMessageDelayed(NEXTPAGE1, 300);
				break;
			default:
				break;
			}
			super.handleMessage(msghand);
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		this.dismiss();

	}

}