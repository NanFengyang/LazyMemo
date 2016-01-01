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
import com.onlan.lazymemo.SettingActivity;
import com.onlan.lazymemo.Http.ClickAppVersion;

public class DialogTwoUtil extends Dialog implements
		android.view.View.OnClickListener {
	private TextView tv;
	private String content = null;

	public DialogTwoUtil(Context context, String content) {
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
		this.setContentView(R.layout.dialogtwoutil_layout);
		tv = (TextView) findViewById(R.id.dialog_tv);
		Button Btn = (Button) findViewById(R.id.dialog_btn);
		Button Btncancel = (Button) findViewById(R.id.dialog_btn_cancel);
		if (content != null && content.trim().length() > 0) {
			tv.setText(Html.fromHtml(content));
		} else {
			tv.setText(Html.fromHtml(APPConfigure.DialogTextShow));
		}
		Btn.setOnClickListener(this);
		Btncancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.dialog_btn:
			this.dismiss();
			if (null != SettingActivity.getSettingActivity()) {
				if (!SettingActivity.getSettingActivity().isUpdaterAPK) {
					SettingActivity.getSettingActivity().lonOut();
				} else {
					ClickAppVersion.downInstallAPK();
				}
			}
			break;
		case R.id.dialog_btn_cancel:
			this.dismiss();
			break;
		default:
			break;
		}
	}
}