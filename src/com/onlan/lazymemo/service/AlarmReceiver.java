package com.onlan.lazymemo.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.onlan.lazymemo.AlarmActivity;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
	
		Intent i = new Intent(context, AlarmActivity.class);
		i.putExtra("content", intent.getExtras().getString("content"));
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}
}