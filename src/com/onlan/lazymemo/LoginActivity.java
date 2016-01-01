package com.onlan.lazymemo;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.color;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onlan.lazymemo.Http.GetServerMemo;
import com.onlan.lazymemo.Http.HttpHandler;
import com.onlan.lazymemo.Http.HttpHandler.OnHttpRequestListener;
import com.onlan.lazymemo.Http.HttpUrlList;
import com.onlan.myView.DialogUtil;
import com.onlan.myView.ToastDialog;
import com.onlan.myView.ToastLoding;

public class LoginActivity extends Activity {
	private HttpHandler mHttpHandler;
	private TextView getYZM;
	private EditText phone;
	private EditText yzm;
	private String TAG = "LoginActivity";
	private ToastLoding mToastLoding;
	private Button login;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		MyApplication.addActivity(this);
		init();
		APPConfigure.setAPPBackGroup(null,
				(RelativeLayout) findViewById(R.id.LoginActivityID),
				APPConfigure.APPBgImgUrl, APPConfigure.APPBgImgID);
	}

	private void init() {
		ImageView back = (ImageView) findViewById(R.id.left_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		getYZM = (TextView) findViewById(R.id.yam_tv);
		phone = (EditText) findViewById(R.id.phone_input);
		yzm = (EditText) findViewById(R.id.yzm_input);
		login = (Button) findViewById(R.id.login);
		getYZM.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getYZM.setTextColor(0xff8d8d8d);
				getYZM.setBackgroundResource(R.drawable.transparent);
				getYZM.setEnabled(false);
				getYzm();
			}
		});
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				login.setEnabled(false);
				login();
			}
		});
	}

	private void getYzm() {
		mHttpHandler = new HttpHandler(LoginActivity.this);
		JSONObject JsonPageNo = new JSONObject();
		try {

			JsonPageNo.put("phoneNum", phone.getText().toString());

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mHttpHandler.addJSONObject(JsonPageNo);
		mHttpHandler.startHandler(HttpUrlList.httpgetVcodeURl);
		mHttpHandler.setOnHttpRequestListener(new OnHttpRequestListener() {

			@Override
			public void startHttpRequest(String message) {
				// TODO Auto-generated method stub
				mToastLoding = new ToastLoding(LoginActivity.this, "验证码发送中");
				mToastLoding.show();
			}

			@Override
			public void onHttpRequestDone(String data) {
				// TODO Auto-generated method stub
				mToastLoding.cancel();
				JsongetVcodeResult(data);
			}

			@Override
			public void onError(String message) {
				// TODO Auto-generated method stub
				mToastLoding.cancel();
				new DialogUtil(LoginActivity.this, message).show();
			}
		});
	}

	private void login() {
		mHttpHandler = new HttpHandler(LoginActivity.this);
		JSONObject JsonPageNo = new JSONObject();
		try {

			JsonPageNo.put("phoneNum", phone.getText().toString());
			JsonPageNo.put("vcode", yzm.getText().toString());

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mHttpHandler.addJSONObject(JsonPageNo);
		mHttpHandler.startHandler(HttpUrlList.httpLoginURl);
		mHttpHandler.setOnHttpRequestListener(new OnHttpRequestListener() {

			@Override
			public void startHttpRequest(String message) {
				// TODO Auto-generated method stub
				mToastLoding = new ToastLoding(LoginActivity.this, "登录中");
				mToastLoding.show();
			}

			@Override
			public void onHttpRequestDone(String data) {
				// TODO Auto-generated method stub
				mToastLoding.cancel();
				JsonloginResult(data);
			}

			@Override
			public void onError(String message) {
				// TODO Auto-generated method stub
				mToastLoding.cancel();
				login.setEnabled(true);
				getYZM.setTextColor(0xffffffff);
				getYZM.setBackgroundResource(R.drawable.btn_bg_un);
				getYZM.setEnabled(true);
				new DialogUtil(LoginActivity.this, message).show();
			}
		});
	}

	private void JsongetVcodeResult(String con) {
		// TODO Auto-generated method stub
		try {
			JSONObject all = new JSONObject(con);
			Log.d(TAG + "JsongetVcodeResult", con);
			Boolean result = all.optBoolean("result");
			int resultId = all.optInt("resultId");
			String resultMSG = all.optString("resultMSG");
			if (!result) {
				getYZM.setTextColor(0xffffffff);
				getYZM.setBackgroundResource(R.drawable.btn_bg_un);
				getYZM.setEnabled(true);
			}

			new DialogUtil(this, resultMSG).show();
			all = null;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void JsonloginResult(String con) {
		// TODO Auto-generated method stub
		try {
			JSONObject all = new JSONObject(con);
			Log.d(TAG + "JsonloginResult", con);
			Boolean result = all.optBoolean("result", false);
			int resultId = all.optInt("resultId");
			String resultMSG = all.optString("resultMSG");
			JSONObject data = all.optJSONObject("data");
			if (data != null) {
				APPConfigure.saveToken(data.optString("token"), phone.getText()
						.toString());
			}

			Log.d(TAG + "resultMSG", resultMSG + "");
			if (result) {
				Log.d(TAG + "123123123123", "1321321321");
				GetServerMemo.startGet(this, 1, 50,
						MyApplication.getMyApplication().mNowTime.NowDate,
						true, false);
				ToastDialog ta = new ToastDialog(this, resultMSG);
				ta.show();
				ta.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						setResult(132);
						finish();
					}
				});

			} else {
				login.setEnabled(true);
				getYZM.setTextColor(0xffffffff);
				getYZM.setBackgroundResource(R.drawable.btn_bg_un);
				getYZM.setEnabled(true);
				new DialogUtil(this, resultMSG).show();
			}

			all = null;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}
}
