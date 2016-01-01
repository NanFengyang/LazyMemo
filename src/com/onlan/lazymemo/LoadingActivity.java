package com.onlan.lazymemo;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onlan.lazymemo.Http.GetAPPTheme;
import com.onlan.lazymemo.Http.GetServerMemo;
import com.onlan.lazymemo.Http.HttpHandler;
import com.onlan.lazymemo.Http.HttpHandler.OnHttpRequestListener;
import com.onlan.lazymemo.Http.HttpUrlList;
import com.onlan.lazymemo.sqlData.ReadLocaMemo;
import com.onlan.myView.ToastDialog;

public class LoadingActivity extends Activity {
	private String TAG = "LoadingActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_loading);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
		MyApplication.addActivity(this);
		showTv = (TextView) findViewById(R.id.show_tv);
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		APPConfigure.Screen_Width = mDisplayMetrics.widthPixels;
		APPConfigure.Screen_Hight = mDisplayMetrics.heightPixels;
		APPConfigure.setAPPBackGroup(null,
				(RelativeLayout) findViewById(R.id.LoadingActivityID),
				APPConfigure.APPBgImgUrl, APPConfigure.APPBgImgID);
		GetAPPTheme.getAppThemePicUrl(this);
	}

	private TextView showTv;
	private final int SHOW1 = 1;
	private final int SHOW2 = 2;
	private final int SHOW3 = 3;
	private final int NEXTPAGE = 5;
	private Handler onstartHandler = new Handler() {
		@Override
		public void handleMessage(Message msghand) {
			switch (msghand.what) {
			case 1:
				showTv.setText("LOADING.");
				onstartHandler.sendEmptyMessageDelayed(SHOW2, 300);
				break;
			case 2:
				showTv.setText("LOADING..");
				onstartHandler.sendEmptyMessageDelayed(SHOW3, 300);
				break;
			case 3:
				showTv.setText("LOADING...");
				onstartHandler.sendEmptyMessageDelayed(SHOW1, 300);

				break;
			case 5:

				/* Create an Intent that will start the Main WordPress Activity. */
				Intent mainIntent = new Intent(LoadingActivity.this,
						MainActivity.class);
				LoadingActivity.this.startActivity(mainIntent);
				LoadingActivity.this.finish();
				break;
			default:
				break;
			}
			super.handleMessage(msghand);
		}
	};

	private static String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + (i >> 24 & 0xFF);
	}

	public static String getIp() {
		WifiManager wm = (WifiManager) MyApplication.getMyApplication()
				.getSystemService(Context.WIFI_SERVICE);
		// 检查Wifi状态
		if (!wm.isWifiEnabled())
			wm.setWifiEnabled(true);
		WifiInfo wi = wm.getConnectionInfo();
		// 获取32位整型IP地址
		int ipAdd = wi.getIpAddress();
		// 把整型地址转换成“*.*.*.*”地址
		String ip = intToIp(ipAdd);
		return ip;
	}

	private void aPPInstall() {
		HttpHandler mHttpHandler = new HttpHandler(LoadingActivity.this);
		JSONObject JsonPageNo = new JSONObject();
		if (!APPConfigure.isAppInstall()) {

			Log.d(TAG, "未注册，开始注册s:" + APPConfigure.isAppInstall());
			TelephonyManager telMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
			String sim = telMgr.getLine1Number();
			String machineModel = android.os.Build.MODEL;
			String machineBrand = android.os.Build.BRAND;
			String osVersion = android.os.Build.VERSION.RELEASE;
			String networkIp = getIp();
			String MachineImei = telMgr.getDeviceId();// 手机设备号获取
			try {
				JsonPageNo.put("platformId", APPConfigure.platformId);
				JsonPageNo.put("appPlatform", 1);
				JsonPageNo.put("imeiCode", MachineImei);
				JsonPageNo.put("simCode", sim);
				JsonPageNo.put("sourceIp", networkIp);
				JsonPageNo.put("mobileBrand", machineBrand);
				JsonPageNo.put("mobileModel", machineModel);
				JsonPageNo.put("osVersion", osVersion);
				JsonPageNo.put("appVersion", APPConfigure.Version);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			mHttpHandler.addJSONObject(JsonPageNo);
			mHttpHandler.startHandler(HttpUrlList.httpAppInstallURl);
			mHttpHandler.setOnHttpRequestListener(new OnHttpRequestListener() {

				@Override
				public void startHttpRequest(String message) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onHttpRequestDone(String data) {
					// TODO Auto-generated method stub
					JsonResult(data);
				}
				@Override
				public void onError(String message) {
					// TODO Auto-generated method stub
					new ToastDialog(LoadingActivity.this, message).show();
				}
			});
		} else {
			Log.d(TAG, "已经注册过了:" + APPConfigure.isAppInstall());
		}
	}

	private void JsonResult(String con) {
		// TODO Auto-generated method stub
		try {
			JSONObject all = new JSONObject(con);
			Log.d(TAG + "JsongetVcodeResult", con);
			Boolean result = all.optBoolean("result");
			int resultId = all.optInt("resultId");
			String resultMSG = all.optString("resultMSG");
			if (result) {
				APPConfigure.saveAppInstall(true);
			} else {
				Log.d(TAG, resultMSG);
			}
			all = null;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		APPConfigure.getAPPFontBG();
		APPConfigure.getAPPfontsize();
		APPConfigure.getAPPToken();
		ReadLocaMemo mReadLocaMemo = new ReadLocaMemo();
		mReadLocaMemo.startRead(this);
		aPPInstall();
		GetServerMemo.startGet(this, 1, 50,
				MyApplication.getMyApplication().mNowTime.NowDate,true,false);
		onstartHandler.sendEmptyMessage(SHOW1);
		onstartHandler.sendEmptyMessageDelayed(NEXTPAGE, 3000);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_loading, menu);
		return true;
	}
}
