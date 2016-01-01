package com.onlan.lazymemo.Http;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.onlan.lazymemo.APPConfigure;
import com.onlan.lazymemo.Http.HttpHandler.OnHttpRequestListener;

import android.app.Activity;
import android.util.Log;

public class GetAPPTheme {
	private static String TAG = "GetAPPTheme";

	public static void getAppThemePicUrl(Activity mactivity) {
		HttpHandler mHttpHandler = new HttpHandler(mactivity);
		JSONObject JsonPageNo = new JSONObject();

		try {
			JsonPageNo.put("pageSize", 1);
			JsonPageNo.put("pageNo", 1);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mHttpHandler.addJSONObject(JsonPageNo);
		mHttpHandler.startHandler(HttpUrlList.httpAppThemeMemoURl);
		mHttpHandler.setOnHttpRequestListener(new OnHttpRequestListener() {

			@Override
			public void startHttpRequest(String message) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onHttpRequestDone(String data) {
				// TODO Auto-generated method stub
				Log.d(TAG + "onHttpRequestDone", data);

				JsonResult(data);
			}

			@Override
			public void onError(String message) {
				// TODO Auto-generated method stub

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
			if (null != data) {
				JSONArray themes = data.optJSONArray("themes");
				if (null != themes) {
					JSONObject themesitem = themes.optJSONObject(0);
					APPConfigure.APPBgImgUrl = themesitem.optString("picUrl");
					Log.d(TAG + "themes", "themes:" + APPConfigure.APPBgImgUrl);
				}
			}

			all = null;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
