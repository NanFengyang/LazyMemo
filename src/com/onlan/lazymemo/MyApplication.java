package com.onlan.lazymemo;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.util.Log;

public class MyApplication extends Application {
	private static List<Activity> mList = new LinkedList<Activity>();
	private static MyApplication mMyApplication;
	public NowTime mNowTime;

	@Override
	public void onCreate() {
		super.onCreate();
		mMyApplication = this;
		mNowTime = new NowTime();
		openDownloadAPKService();
	}

	public static MyApplication getMyApplication() {
		return mMyApplication;

	}

	public void openDownloadAPKService() {
		Intent intent = new Intent(MyApplication.this,
				com.onlan.lazymemo.service.DownloadAPKService.class);
		startService(intent);
	}

	// add Activity
	public static void addActivity(Activity activity) {
		Log.e("activityName", activity.getLocalClassName());
		mList.add(activity);
	}

	public void openLocation() {
		Intent intent = new Intent(
				"com.onlan.lazymemo.service.GaodeLocationService");
		intent.putExtra("enabled", true);
		startService(intent);// 启动定位服务
	}

	public void closeLocation() {
		Intent intent = new Intent(
				"com.onlan.lazymemo.service.GaodeLocationService");
		intent.putExtra("enabled", false);
		stopService(intent);// 关闭定位服务
	}

	public class NowTime {
		@SuppressWarnings("unused")
		public int year;
		@SuppressWarnings("unused")
		public int month;
		@SuppressWarnings("unused")
		public int day;
		@SuppressWarnings("unused")
		public int hour;
		@SuppressWarnings("unused")
		public int minute;
		public String NowDate;

		public NowTime() {
			Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH) + 1;
			day = c.get(Calendar.DAY_OF_MONTH);
			hour = c.get(Calendar.HOUR_OF_DAY);
			minute = c.get(Calendar.MINUTE);
			NowDate = year + "/" + month + "/" + day;
		}
	}

	public void exit() {
		try {
			for (int i = 0; i < mList.size(); i++) {
				// 在程序退出(Activity销毁）时销毁悬浮窗口
				Log.e("activityName", mList.get(i).getLocalClassName());
				if (mList.get(i) != null)
					mList.get(i).finish();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);

		}
	}
}
