package com.onlan.myView;

import com.onlan.lazymemo.MyApplication;

import android.widget.Toast;

public class ToastUitil {

	public static void make(String str) {
		Toast.makeText(MyApplication.getMyApplication(), str,
				Toast.LENGTH_SHORT).show();
	}
}
