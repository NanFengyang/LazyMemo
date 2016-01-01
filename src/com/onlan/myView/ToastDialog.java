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

public class ToastDialog extends Dialog implements
		android.view.View.OnClickListener {
	private TextView tv;
	private String content = null;

	public ToastDialog(Context context, String content) {
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
		Btn.setVisibility(View.GONE);
		onstartHandler.sendEmptyMessageDelayed(NEXTPAGE, 2000);
	}

	private final int NEXTPAGE = 1;
	private Handler onstartHandler = new Handler() {
		@Override
		public void handleMessage(Message msghand) {
			switch (msghand.what) {
			case 1:
				ToastDialog.this.cancel();
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

	private OnToastDialogListener onToastDialogListener;

	public void setOnHttpRequestListener(
			OnToastDialogListener mOnToastDialogListener) {
		this.onToastDialogListener = mOnToastDialogListener;
	}

	/**
	 * 下面是一个自定义的回调函数，用到回调http请求是否完成
	 * 
	 * @author shimingzheng
	 * 
	 */
	public static interface OnToastDialogListener {
		/**
		 * 请求完成
		 * 
		 * @param responseCode
		 * @param message
		 */
		void onToastDialogDismiss();

		/**
		 * 请求中
		 * 
		 * @param uploadSize
		 */
		void onError(String message);

		/**
		 * 开始请求
		 * 
		 * @param fileSize
		 */
		void onShow();
	}
}