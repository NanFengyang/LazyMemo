package com.onlan.lazymemo.Http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.onlan.lazymemo.APPConfigure;

public class HttpHandler {
	private static final int START_DOWNLOAD_MESSAGE = 0x01;
	private static final int FINISH_DOWNLOAD_MESSAGE = 0x02;
	private static final int ERROR_DOWNLOAD_MESSAGE = 0x03;
	private static final int TIME_OUT_DOWNLOAD_MESSAGE = 0x04;
	private Context mcontext;
	private Handler myHandler;
	private JSONObject mJSONObject;
	private HttpPost httpRequest;

	private String TAG = "HttpHandler";

	public HttpHandler(Context mcontext) {
		this.mcontext = mcontext;
	}

	public void startHandler(String url) {
		myHandler = new MyHandler();
		httpRequest = new HttpPost(url);
		httpRequest.addHeader("token", APPConfigure.APP_Token);
		httpRequest.addHeader("phoneNum", APPConfigure.APP_PhoneNum);
		httpRequest.addHeader("Accept-Encoding", "gzip");
		ThreadPoolUtils.execute(new MyRunnable());
		myHandler.sendEmptyMessageDelayed(TIME_OUT_DOWNLOAD_MESSAGE,
				HttpClientHelper.getConnectionTimeout());
	}

	public void addHeader(String key, String value) {
		if (httpRequest != null) {
			httpRequest.addHeader(key, value);
		}
	}

	public void setTimeout(int time) {
		HttpClientHelper.setTimeout(time);
	}

	public void setsotimeout(int time) {
		HttpClientHelper.setsotimeout(time);
	}

	public void setConnectionTimeout(int time) {
		HttpClientHelper.setConnectionTimeout(time);
	}

	public void addJSONObject(JSONObject jSONObject) {
		mJSONObject = null;
		mJSONObject = jSONObject;
	}

	private class MyRunnable implements Runnable {
		@Override
		public void run() {
			myHandler.sendEmptyMessage(START_DOWNLOAD_MESSAGE);
			try {
				if (mJSONObject != null) {
					Log.d(TAG, mJSONObject.toString());
					httpRequest.setEntity(new StringEntity(mJSONObject
							.toString(), "utf-8"));
				}
				HttpResponse httpResponse = HttpClientHelper.getHttpClient()
						.execute(httpRequest);
				String result = HttpGetGzip.getJsonStringFromGZIP(httpResponse);
				Log.d(TAG, result);
				if (isHttpSuccessExecuted(httpResponse)) {

					Message msg = myHandler.obtainMessage();
					msg.what = FINISH_DOWNLOAD_MESSAGE;
					msg.obj = result;
					myHandler.sendMessage(msg);
				} else {
					Message msg = myHandler.obtainMessage();
					msg.what = ERROR_DOWNLOAD_MESSAGE;
					msg.obj = result;
					myHandler.sendMessage(msg);

				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d(TAG, e.toString());
				Message msg = myHandler.obtainMessage();
				msg.what = ERROR_DOWNLOAD_MESSAGE;
				msg.obj = "服务器响应超时！";
				myHandler.sendMessage(msg);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();

				Message msg = myHandler.obtainMessage();
				msg.what = ERROR_DOWNLOAD_MESSAGE;
				msg.obj = e.toString();
				myHandler.sendMessage(msg);
			} catch (IOException e) {
				e.printStackTrace();

				Message msg = myHandler.obtainMessage();
				msg.what = ERROR_DOWNLOAD_MESSAGE;
				msg.obj = e.toString();
				myHandler.sendMessage(msg);
			} catch (Exception ex) {
				ex.printStackTrace();
				Log.d(TAG, ex.toString());
				Message msg = myHandler.obtainMessage();
				msg.what = ERROR_DOWNLOAD_MESSAGE;
				msg.obj = "服务器连接失败。请检查网络。";
				myHandler.sendMessage(msg);
				HttpClientHelper.getHttpClient().getConnectionManager()
						.shutdown();
			}
		}
	}

	public static boolean isHttpSuccessExecuted(HttpResponse response) {
		int statusCode = response.getStatusLine().getStatusCode();
		return (statusCode > 199) && (statusCode < 400);
	}

	private class MyHandler extends Handler {
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case START_DOWNLOAD_MESSAGE:

				if (onHttpRequestListener != null) {
					onHttpRequestListener.startHttpRequest("开始请求");
				}
				break;

			case FINISH_DOWNLOAD_MESSAGE:
				myHandler.removeMessages(TIME_OUT_DOWNLOAD_MESSAGE);
				if (onHttpRequestListener != null) {
					onHttpRequestListener.onHttpRequestDone(msg.obj.toString());
				}

				break;

			case ERROR_DOWNLOAD_MESSAGE:
				myHandler.removeMessages(TIME_OUT_DOWNLOAD_MESSAGE);
				if (onHttpRequestListener != null) {
					onHttpRequestListener.onError(msg.obj.toString());
				}
				break;

			case TIME_OUT_DOWNLOAD_MESSAGE:
				if (onHttpRequestListener != null) {
					onHttpRequestListener.onError("服务器连接失败。请检查网络。");
				}
				break;
			default:
				System.out.println("nothing to do");
				break;
			}
		}
	}

	private OnHttpRequestListener onHttpRequestListener;

	public void setOnHttpRequestListener(
			OnHttpRequestListener mOnHttpRequestListener) {
		this.onHttpRequestListener = mOnHttpRequestListener;
	}

	/**
	 * 下面是一个自定义的回调函数，用到回调http请求是否完成
	 * 
	 * @author shimingzheng
	 * 
	 */
	public static interface OnHttpRequestListener {
		/**
		 * 请求完成
		 * 
		 * @param data
		 */
		void onHttpRequestDone(String data);

		/**
		 * 请求中
		 * 
		 * @param message
		 */
		void onError(String message);

		/**
		 * 开始请求
		 * 
		 * @param message
		 */
		void startHttpRequest(String message);
	}

}
