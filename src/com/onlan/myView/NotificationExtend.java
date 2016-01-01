package com.onlan.myView;

import java.util.List;

import com.onlan.lazymemo.MainActivity;
import com.onlan.lazymemo.R;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationExtend {
	private Activity context;
	private final String TAG = "NotificationExtend";
	private boolean flag = false;

	public NotificationExtend(Activity context) {
		this.context = context;
	}

	public void showNotification(String title, String content) {
		showNotification_rl(title, content);

	}

	// 显示Notification
	public void showNotification_rl(String title, String content) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);

		Notification notification = new Notification(R.drawable.logo, "便签墙",
				System.currentTimeMillis());

		notification.flags |= Notification.FLAG_ONGOING_EVENT;

		// notification.flags |= Notification.FLAG_NO_CLEAR;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		// notification.defaults = Notification.DEFAULT_LIGHTS;
		// notification.ledARGB = Color.BLUE;
		// notification.ledOnMS = 5000;
		CharSequence contentTitle = title;
		CharSequence contentText = content;

		Intent notificationIntent = new Intent(context, MainActivity.class);
		notificationIntent.setAction(Intent.ACTION_MAIN);
		notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);

		notificationManager.notify(TAG, 5, notification);
		flag = true;

	}

	// 取消显示֪
	public void cancelNotification() {
		if (flag) {
			NotificationManager notificationManager = (NotificationManager) context
					.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(TAG, 5);
			flag = false;
		}
	}

	public boolean isAppOnBackground() {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		// Returns a list of application processes that are running on the
		// device
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		String packageName = context.getPackageName();
		if (appProcesses == null || packageName == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			Log.e(TAG, "---isAppOnForeground------" + appProcess.processName
					+ "  packageName:" + packageName);
			if (appProcess.processName.equals(packageName)
					&& ((appProcess.importance == RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE))) {
				return true;
			}
		}

		return false;
	}
}
