package com.onlan.lazymemo;

import java.io.IOException;
import java.util.Calendar;

import com.onlan.myView.NotificationExtend;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;

public class AlarmActivity extends Activity {
	private MediaPlayer mp;
	private NotificationExtend mNotificationExtend;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mp = new MediaPlayer();
		startMusic();
		showNotificationExtend();
	}

	public void showNotificationExtend() {
		mNotificationExtend = new NotificationExtend(this);
		mNotificationExtend.showNotification("便签墙提醒您：", getIntent().getExtras().getString("content"));
		this.finish();
	}

	public void showAlack() {
		String content = "闹钟时间到！";
		new AlertDialog.Builder(AlarmActivity.this).setTitle("便签墙提醒：")
				.setMessage(content).setCancelable(false)
				.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						mp.stop();
						finish();
					}
				}).setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						mp.stop();
						finish();
					}
				}).show();
	}

	@SuppressWarnings("unused")
	private class NowTime {
		public String date;
		public String time;

		public NowTime() {
			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int day = c.get(Calendar.DAY_OF_MONTH);
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);
			date = year + "/" + month + "/" + day;
			time = hour + ":" + minute;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	private void startMusic() {

		try {
			mp.setDataSource(this,
					RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
			mp.prepare();
			mp.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
