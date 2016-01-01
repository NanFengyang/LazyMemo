package com.onlan.lazymemo.Http;

import org.json.JSONException;
import org.json.JSONObject;

import com.onlan.lazymemo.APPConfigure;
import com.onlan.lazymemo.SettingActivity;
import com.onlan.lazymemo.Http.HttpHandler.OnHttpRequestListener;
import com.onlan.lazymemo.service.DownloadAPKService;
import com.onlan.myView.DialogTwoUtil;
import com.onlan.myView.DialogUtil;
import com.onlan.myView.ToastDialog;
import com.onlan.myView.ToastLoding;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ClickAppVersion {
	private static ToastLoding mToastLoding;
	private static HttpHandler mHttpHandler;
	private static Activity mActivity;
	private static String TAG = "ClickAppVersion";
	/**
	 * type,版本检测的来源。1为系统设置的版本检测，2为首页版本检测。
	 */
	private static int type;

	public static void startClickAppVersion(Activity activity, int mtype) {
		type = mtype;
		mActivity = activity;
		if (mtype == 1) {
			mToastLoding = new ToastLoding(activity, "检测新版本中");
			mToastLoding.show();
		}

		mhandle.sendEmptyMessageDelayed(CLICK_VERSION, 1000);
	}

	private final static int CLICK_VERSION = 1;
	private static Handler mhandle = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case CLICK_VERSION:
				ClickAppVersion();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	private static void ClickAppVersion() {
		mHttpHandler = new HttpHandler(mActivity);
		JSONObject JsonPageNo = new JSONObject();
		String osVersion = android.os.Build.VERSION.RELEASE;
		try {
			JsonPageNo.put("platformId", APPConfigure.platformId);
			JsonPageNo.put("terminalType", 1);
			JsonPageNo.put("osVersion", osVersion);
			JsonPageNo.put("versionNum", APPConfigure.versionNum);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mHttpHandler.addJSONObject(JsonPageNo);
		mHttpHandler.startHandler(HttpUrlList.httpAppVersionURl);
		mHttpHandler.setOnHttpRequestListener(new OnHttpRequestListener() {

			@Override
			public void startHttpRequest(String message) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onHttpRequestDone(String data) {
				// TODO Auto-generated method stub
				Log.d(TAG + "onHttpRequestDone", data);
				if (mToastLoding != null && mToastLoding.isShowing()) {
					mToastLoding.cancel();
				}
				JsonResult(data);
			}

			@Override
			public void onError(String message) {
				// TODO Auto-generated method stub
				if (mToastLoding != null && mToastLoding.isShowing()) {
					mToastLoding.cancel();
				}
				new ToastDialog(mActivity, message).show();
			}
		});
	}

	private static void JsonResult(String con) {
		// TODO Auto-generated method stub
		try {

			JSONObject all = new JSONObject(con);
			Log.d(TAG + "JsonResult", con);
			Boolean result = all.optBoolean("result");
			int resultId = all.optInt("resultId");
			String resultMSG = all.optString("resultMSG");
			JSONObject data = all.optJSONObject("data");
			if (mToastLoding != null && mToastLoding.isShowing()) {
				mToastLoding.cancel();
			}
			if (result) {
				if (resultId == 1007) {
					if (null != data) {
						newestVersion = data.optString("newestVersion");
						apkUrl = data.optString("updateUrl");
						String str = "最新版本号:V" + newestVersion + "<br/>更新描述:"
								+ data.optString("newestInfo") + "<br/><br/>"
								+ "是否更新？";
						if (data.optInt("mustUpdate") == 0) {
							new DialogTwoUtil(mActivity, str).show();
						} else {
							downInstallAPK();
						}
					}
				} else if (resultId == 1008) {
					if (type == 1) {
						new DialogUtil(mActivity, resultMSG).show();
					}

				}
			} else {
				APPConfigure.CheckToken(mActivity, resultId, resultMSG);
			}

			all = null;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String apkUrl;
	private static String newestVersion;

	public static void downInstallAPK() {
		DownloadAPKService.downNewFile(apkUrl, 101, "便签墙:V" + newestVersion);
	}

}
