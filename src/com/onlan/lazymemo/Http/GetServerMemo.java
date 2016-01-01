package com.onlan.lazymemo.Http;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.onlan.lazymemo.APPConfigure;
import com.onlan.lazymemo.MainActivity;
import com.onlan.lazymemo.MyApplication;
import com.onlan.lazymemo.Http.HttpHandler.OnHttpRequestListener;
import com.onlan.lazymemo.service.AlarmReceiver;
import com.onlan.mainlistViewObject.Group;
import com.onlan.mainlistViewObject.ListItem;
import com.onlan.myView.ToastDialog;
import com.onlan.myView.ToastLoding;

public class GetServerMemo {
	private static HttpHandler mHttpHandler;
	private static Activity mactivity;
	static String Tag = "GetServerMemo";
	private static ArrayList<ServerMemo> ServerMemoList;
	private static ArrayList<Group> HistorygroupList;
	private static ArrayList<List<ListItem>> HistorygchildList;
	private static ArrayList<Group> NowgroupList;
	private static ArrayList<List<ListItem>> NowchildList;
	private static ToastLoding mToastLoding;

	public ArrayList<Group> getHistorygroupList() {
		return HistorygroupList;
	}

	public ArrayList<Group> getNowgroupList() {
		return NowgroupList;
	}

	public ArrayList<List<ListItem>> getHistorygchildList() {
		return HistorygchildList;
	}

	public ArrayList<List<ListItem>> getNowchildList() {
		return NowchildList;
	}

	/**
	 * 
	 * @param activity
	 * @param pageNo
	 * @param pagesize
	 * @param date
	 * @param ifAfter
	 *            true，查询以后的(当前和未来)，false，查询以前的(历史)
	 * @param isToastLoding
	 */
	public static void startGet(Activity activity, int pageNo, int pagesize,
			String date, final Boolean ifAfter, final Boolean isToastLoding) {
		if (isToastLoding) {
			mToastLoding = new ToastLoding(activity, "努力获取中");
			mToastLoding.show();
		}
		Log.d(Tag + "date", date);
		mactivity = activity;
		mHttpHandler = new HttpHandler(activity);
		JSONObject JsonPageNo = new JSONObject();
		try {
			JsonPageNo.put("pageNo", pageNo);
			JsonPageNo.put("pageSize", pagesize);
			JsonPageNo.put("date", date);
			JsonPageNo.put("ifAfter", ifAfter);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mHttpHandler.addJSONObject(JsonPageNo);
		mHttpHandler.startHandler(HttpUrlList.httpgetMemoListURl);
		mHttpHandler.setOnHttpRequestListener(new OnHttpRequestListener() {

			@Override
			public void startHttpRequest(String message) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onHttpRequestDone(String data) {
				// TODO Auto-generated method stub
				Log.d(Tag + "JsongetVcodeResult", data);

				JsonResult(data);
				if (isToastLoding && null != MainActivity.getMainActivity()) {
					if (ifAfter) {
						MainActivity.getMainActivity().clickNowData();
					} else {
						MainActivity.getMainActivity().clickHisData();
					}

				}
				if (null != mToastLoding && mToastLoding.isShowing()) {
					mToastLoding.cancel();
				}
			}

			@Override
			public void onError(String message) {
				// TODO Auto-generated method stub
				if (null != mToastLoding && mToastLoding.isShowing()) {
					mToastLoding.cancel();
				}
				new ToastDialog(mactivity, message).show();

			}
		});
	}

	private static void JsonResult(String con) {
		// TODO Auto-generated method stub
		try {
			ServerMemoList = new ArrayList<ServerMemo>();
			JSONObject all = new JSONObject(con);
			Log.d(Tag + "JsongetVcodeResult", con);
			Boolean result = all.optBoolean("result");
			String resultMSG = all.optString("resultMSG");
			if (result) {
				APPConfigure.UserIsLogin = true;
				JSONObject data = all.optJSONObject("data");
				if (null != data) {
					JSONArray memo = data.optJSONArray("memos");
					if (null != memo) {
						for (int i = 0; i < memo.length(); i++) {
							Log.d(Tag + "memo", i + "");
							JSONObject item = memo.optJSONObject(i);
							ServerMemo mServerMemo = new ServerMemo();
							mServerMemo.phoneNumber = item
									.optString("phoneNumber");
							mServerMemo.type = item.optString("type");
							mServerMemo.content = item.optString("content");
							mServerMemo.addTime = item.optString("addTime");
							mServerMemo.updateTime = item
									.optString("updateTime");
							mServerMemo.ifAlarm = item.optString("ifAlarm");
							mServerMemo.id = item.optString("id");
							mServerMemo.advancedTime = item
									.optString("advancedTime");
							mServerMemo.alarmTime = item.optString("alarmTime");
							mServerMemo.fromPhone = item.optString("fromPhone");
							String str = item.optString("taskTime");
							mServerMemo.taskDate = str.substring(0,
									str.length() - 5);
							mServerMemo.taskTime = str.substring(
									str.length() - 5, str.length());
							ServerMemoList.add(mServerMemo);
						}
					}
				}
			} else {
				APPConfigure.UserIsLogin = false;
				APPConfigure.APP_Token = "";
				APPConfigure.APP_PhoneNum = "";

			}
			all = null;
			handleData();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Boolean isaddGroup(String date) {
		Boolean adddate = true;
		if (NowgroupList.size() > 0) {
			for (Group group : NowgroupList) {
				if (group.getTitle().toString().equals(date)) {
					adddate = false;
				} else {
					adddate = true;
				}
			}

		} else {
			adddate = true;
		}
		return adddate;

	}

	private static Boolean ishisaddGroup(String date) {
		Boolean adddate = true;
		if (HistorygroupList.size() > 0) {
			for (Group group : HistorygroupList) {
				if (group.getTitle().toString().equals(date)) {
					adddate = false;
				} else {
					adddate = true;
				}
			}

		} else {
			adddate = true;
		}
		return adddate;

	}

	private static String DateTimeFormatPattern = "yyyy/MM/dd";

	private static void handleData() {
		Log.d(Tag + "handleData", "handleData");
		HistorygroupList = new ArrayList<Group>();
		HistorygchildList = new ArrayList<List<ListItem>>();
		NowgroupList = new ArrayList<Group>();
		NowchildList = new ArrayList<List<ListItem>>();
		if (ServerMemoList != null && ServerMemoList.size() > 0) {
			for (ServerMemo mServerMemo : ServerMemoList) {
				SimpleDateFormat format = new SimpleDateFormat(
						DateTimeFormatPattern);
				Log.d(Tag + "handleData", "handleData1");
				try {
					Date date1 = format.parse(mServerMemo.taskDate);
					Date date2 = format
							.parse(MyApplication.getMyApplication().mNowTime.NowDate);
					Log.d(Tag + "handleData", "handleData2");
					if (date1.getTime() < date2.getTime()) {
						if (ishisaddGroup(mServerMemo.taskDate)) {
							Group hisgroup = new Group();
							hisgroup.setTitle(mServerMemo.taskDate);
							HistorygroupList.add(hisgroup);
						}
					} else {
						if (isaddGroup(mServerMemo.taskDate)) {
							Group group = new Group();
							group.setTitle(mServerMemo.taskDate);
							NowgroupList.add(group);
						}
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d(Tag + "handleData", e.toString());
				}
			}

		}

		if (NowgroupList.size() > 0) {
			Log.d(Tag + "handleData", "handleData3");

			for (Group mgroup : NowgroupList) {
				List<ListItem> childTemp = new ArrayList<ListItem>();
				Log.d("MainActivity", "mgroup:" + mgroup.getTitle().toString());
				int id = 1010;
				for (ServerMemo mServerMemo : ServerMemoList) {
					if (mgroup.getTitle().equals(mServerMemo.taskDate)) {
						ListItem mlistitem = new ListItem();
						mlistitem.time = mServerMemo.taskTime;
						mlistitem.content = mServerMemo.content;
						mlistitem.id = Integer.parseInt(mServerMemo.id);
						mlistitem.isalarmclock = Integer
								.parseInt(mServerMemo.ifAlarm);
						mlistitem.type = Integer.parseInt(mServerMemo.type);
						mlistitem.date = mServerMemo.taskDate;
						mlistitem.bgcolor = -1;
						mlistitem.isLocal = false;
						mlistitem.FormPhone = mServerMemo.fromPhone;
						String[] date = mlistitem.date.trim().split("/");
						int year = 0;
						int mouth = 0;
						int day = 0;
						if (date != null && date.length == 3) {
							year = Integer.parseInt(date[0]);
							mouth = Integer.parseInt(date[1]);
							day = Integer.parseInt(date[2]);
						}
						String[] time = mlistitem.time.split(":");
						int hour = 0;
						int mini = 0;
						if (time != null && time.length == 2) {
							hour = Integer.parseInt(time[0]);
							mini = Integer.parseInt(time[1]);
						}
						if (mlistitem.isalarmclock == 1) {
							// setAlarmClock(id, year, mouth, day, hour, mini);
							id++;
						}

						childTemp.add(mlistitem);
					}
				}
				NowchildList.add(childTemp);
			}
		}

		if (HistorygroupList.size() > 0) {
			Log.d(Tag + "handleData", "handleData4");
			for (Group mgroup : HistorygroupList) {
				List<ListItem> hischildTemp = new ArrayList<ListItem>();
				for (ServerMemo mServerMemo : ServerMemoList) {
					if (mgroup.getTitle().equals(mServerMemo.taskDate)) {
						ListItem mlistitem = new ListItem();
						mlistitem.time = mServerMemo.taskTime;
						mlistitem.content = mServerMemo.content;
						mlistitem.id = Integer.parseInt(mServerMemo.id);
						mlistitem.isalarmclock = Integer
								.parseInt(mServerMemo.ifAlarm);
						mlistitem.type = Integer.parseInt(mServerMemo.type);
						mlistitem.date = mServerMemo.taskDate;
						mlistitem.bgcolor = -1;
						mlistitem.isLocal = false;
						mlistitem.FormPhone = mServerMemo.fromPhone;
						hischildTemp.add(mlistitem);
					}
				}
				HistorygchildList.add(hischildTemp);
			}

		}
	}

	private static void setAlarmClock(int id, int year, int mouth, int day,
			int hourOfDay, int minute) {
		Calendar c = Calendar.getInstance();// 获取日期对象
		c.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		c.setTimeInMillis(System.currentTimeMillis()); // 设置Calendar对象
		c.set(Calendar.YEAR, year); // 设置闹钟年
		c.set(Calendar.MONTH, mouth - 1); // 设置闹钟月
		c.set(Calendar.DAY_OF_MONTH, day); // 设置闹钟日
		c.set(Calendar.HOUR_OF_DAY, hourOfDay); // 设置闹钟小时数
		c.set(Calendar.MINUTE, minute); // 设置闹钟的分钟数
		c.set(Calendar.SECOND, 0); // 设置闹钟的秒数
		c.set(Calendar.MILLISECOND, 0); // 设置闹钟的毫秒数
		Intent intent = new Intent(mactivity, AlarmReceiver.class); // 创建Intent对象
		PendingIntent pi = PendingIntent.getBroadcast(mactivity, id, intent, 0); // 创建PendingIntent
		AlarmManager alarmManager = (AlarmManager) mactivity
				.getSystemService(mactivity.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);

	}

	static class ServerMemo {
		public String id;
		public String phoneNumber;
		public String type;
		public String content;
		public String taskDate;
		public String taskTime;
		public String addTime;
		public String updateTime;
		public String ifAlarm;
		public String advancedTime;
		public String alarmTime;
		public String fromPhone;
	}
}
