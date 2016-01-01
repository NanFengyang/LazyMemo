package com.onlan.lazymemo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.PoiItem;
import com.onlan.lazymemo.ContactPhone.ContartsPopupWindow;
import com.onlan.lazymemo.Http.GetServerMemo;
import com.onlan.lazymemo.Http.HttpHandler;
import com.onlan.lazymemo.Http.HttpHandler.OnHttpRequestListener;
import com.onlan.lazymemo.Http.HttpUrlList;
import com.onlan.lazymemo.service.AlarmReceiver;
import com.onlan.lazymemo.service.GaodeLocationService;
import com.onlan.lazymemo.service.GaodeLocationService.OnGaodeLocationListener;
import com.onlan.lazymemo.sqlData.MemoInfo;
import com.onlan.lazymemo.sqlData.MemoInfoService;
import com.onlan.myView.DialogUtil;
import com.onlan.myView.LocationAddressPopupWindow;
import com.onlan.myView.ToastDialog;
import com.onlan.myView.ToastLoding;

@TargetApi(11)
public class NewMemoActivity extends Activity implements OnClickListener,
		OnGaodeLocationListener {
	private static NewMemoActivity mNewMemoActivity;
	private String TAG = "NewMemoActivity";
	private TextView memoDate;
	private TextView memoTime;
	private int groupid;
	private int childid;
	private MemoInfoService mMemoInfoService;
	private EditText memocontent;
	private RadioButton memoAlamClock;
	private int year;
	private int mouth;
	private int day;
	private int hour;
	private int minutes;
	private RelativeLayout Fontbg_default;
	private RelativeLayout Fontbg_1;
	private RelativeLayout Fontbg_2;
	private RelativeLayout Fontbg_3;
	private RelativeLayout Fontbg_4;
	private RelativeLayout Fontbg_5;
	private RelativeLayout Fontbg_6;
	private RelativeLayout Fontbg_7;
	private int nowBgcorlor = -1;
	private EditText inputFriendPhone;
	private RadioButton memoIssend;
	private RadioButton memoIssendCommon;
	private HttpHandler mHttpHandler;
	private RelativeLayout mLinearLayoutADD;
	private String JG = ":";
	private ToastLoding mToastLoding;
	private ArrayList<Map<String, String>> contratsList;
	private List<PoiItem> getGestionCities;
	public static NewMemoActivity getNewMemoActivity() {
		return mNewMemoActivity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_new_memo);
		MyApplication.addActivity(this);
		nowBgcorlor = -1;
		mNewMemoActivity = this;
		GaodeLocationService.setOnGaodeLocationListener(this);
		init();
		APPConfigure.setAPPBackGroup(
				(LinearLayout) findViewById(R.id.NewMemoActivityID), null,
				APPConfigure.APPBgImgUrl, APPConfigure.APPBgImgID);
	}

	@TargetApi(11)
	private void init() {
		TextView title = (TextView) findViewById(R.id.title_tv);
		TextView commit = (TextView) findViewById(R.id.commit);
		ImageView back = (ImageView) findViewById(R.id.left_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setResult(50);
				finish();
			}
		});
		mLinearLayoutADD = (RelativeLayout) findViewById(R.id.input_issendshowlayout);
		memocontent = (EditText) findViewById(R.id.content_edit);
		memoDate = (TextView) findViewById(R.id.tv_datecontent);
		memoTime = (TextView) findViewById(R.id.tv_timecontent);
		memoDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent calandar = new Intent();
				calandar.setClass(NewMemoActivity.this, CalendarActivity.class);
				startActivityForResult(calandar, 102);
			}
		});
		memoAlamClock = (RadioButton) findViewById(R.id.rb_alarmclockcontent);
		memoAlamClock.setTag("False");
		memoAlamClock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (memoAlamClock.getTag() == "False") {
					memoAlamClock.setChecked(true);
					memoAlamClock.setTag("True");
				} else {
					memoAlamClock.setChecked(false);
					memoAlamClock.setTag("False");
				}
			}
		});
		memoTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TimePickerDialog time = new TimePickerDialog(
						NewMemoActivity.this,
						android.R.style.Theme_DeviceDefault_Panel,
						new OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								// TODO Auto-generated method stub
								hour = hourOfDay;
								minutes = minute;
								String hour = String.valueOf(hourOfDay);
								String minu = String.valueOf(minute);
								if (hourOfDay < 10) {
									hour = "0" + hour;
								}
								if (minutes < 10) {
									minu = "0" + minu;
								}
								memoTime.setText(hour + JG + minu);
							}
						}, MyApplication.getMyApplication().mNowTime.hour,
						MyApplication.getMyApplication().mNowTime.minute, true);
				time.setCancelable(true);
				time.setCanceledOnTouchOutside(true);
				time.show();
			}
		});
		inputFriendPhone = (EditText) findViewById(R.id.input_issendshow);
		memoIssend = (RadioButton) findViewById(R.id.rb_ssendcontent);
		memoIssend.setTag("False");
		memoIssend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (memoIssend.getTag() == "False") {
					if (APPConfigure.UserIsLogin) {
						memoIssend.setChecked(true);
						memoIssend.setTag("True");
						mLinearLayoutADD.setVisibility(View.VISIBLE);
					} else {
						memoIssend.setChecked(false);
						memoIssend.setTag("False");
						new DialogUtil(NewMemoActivity.this, "登录后才能发给好友哦。")
								.show();
					}
				} else {
					memoIssend.setChecked(false);
					memoIssend.setTag("False");
					mLinearLayoutADD.setVisibility(View.GONE);
					inputFriendPhone.setText("");
				}
			}
		});
		memoIssendCommon = (RadioButton) findViewById(R.id.rb_ssendcommoncontent);
		memoIssendCommon.setTag("False");
		memoIssendCommon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (memoIssendCommon.getTag() == "False") {
					if (APPConfigure.UserIsLogin) {
						mToastLoding = new ToastLoding(NewMemoActivity.this,
								"正在定位,请稍等");
						mToastLoding.show();
						MyApplication.getMyApplication().openLocation();
						GaodeLocationService.setIsPoiAroundSearch(true);
					} else {
						memoIssendCommon.setChecked(false);
						memoIssendCommon.setTag("False");
						new DialogUtil(NewMemoActivity.this, "登录后才能发到公共便签墙。")
								.show();
					}
				} else {
					memoIssendCommon.setChecked(false);
					memoIssendCommon.setTag("False");
				}
			}
		});
		commit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!memoIssend.isChecked() && !memoIssendCommon.isChecked()) {
					if (getIntent().getExtras().getString("isedit")
							.equals("true")) {
						editMemo();
					} else {
						addmemo();
					}
				} else {
					if (memoIssend.isChecked()) {
						if (inputFriendPhone.getText().toString().length() > 0) {
							sendMemoToserver(false);
						} else {
							new DialogUtil(NewMemoActivity.this, "请填写发送手机号！")
									.show();
							return;
						}
					}
					if (memoIssendCommon.isChecked()) {
						sendMemoToserver(true);
					}
				}

			}
		});
		ImageView addMoreContart = (ImageView) findViewById(R.id.input_moreadd);
		addMoreContart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				openContacts();
			}
		});
		if (this.getIntent().getExtras().getString("isedit").equals("true")) {
			title.setText("编辑备忘");
			groupid = this.getIntent().getExtras().getInt("groupid");
			childid = this.getIntent().getExtras().getInt("childid");
			if (this.getIntent().getExtras().getBoolean("isHistory")) {
				memocontent
						.setText(MainActivity.getMainActivity().historygchildList
								.get(groupid).get(childid).content);
				memoTime.setText(MainActivity.getMainActivity().historygchildList
						.get(groupid).get(childid).time);
				memoDate.setText(MainActivity.getMainActivity().historygchildList
						.get(groupid).get(childid).date);
				if (MainActivity.getMainActivity().historygchildList.get(
						groupid).get(childid).isalarmclock == 1) {
					memoAlamClock.setChecked(true);
					memoAlamClock.setTag("True");
				}
				if (MainActivity.getMainActivity().historygchildList.get(
						groupid).get(childid).isLocal) {
					memoIssend.setChecked(false);
					memoIssend.setTag("False");
				} else {
					memoIssend.setChecked(true);
					memoIssend.setTag("True");
				}
			} else {
				memocontent.setText(MainActivity.getMainActivity().childList
						.get(groupid).get(childid).content);
				memoTime.setText(MainActivity.getMainActivity().childList.get(
						groupid).get(childid).time);
				memoDate.setText(MainActivity.getMainActivity().childList.get(
						groupid).get(childid).date);
				if (MainActivity.getMainActivity().childList.get(groupid).get(
						childid).isalarmclock == 1) {
					memoAlamClock.setChecked(true);
					memoAlamClock.setTag("True");
				}
				if (MainActivity.getMainActivity().childList.get(groupid).get(
						childid).isLocal) {
					memoIssend.setChecked(false);
					memoIssend.setTag("False");
				} else {
					memoIssend.setChecked(true);
					memoIssend.setTag("True");
				}
			}

		} else {
			hour = MyApplication.getMyApplication().mNowTime.hour;
			minutes = MyApplication.getMyApplication().mNowTime.minute;
			year = MyApplication.getMyApplication().mNowTime.year;
			mouth = MyApplication.getMyApplication().mNowTime.month;
			day = MyApplication.getMyApplication().mNowTime.day;
			String hour1 = String.valueOf(hour);
			String minu1 = String.valueOf(minutes);
			String mouth1 = String.valueOf(mouth);
			String day1 = String.valueOf(day);
			if (mouth < 10) {
				mouth1 = "0" + mouth1;
			}
			if (day < 10) {
				day1 = "0" + day1;
			}
			if (hour < 10) {
				hour1 = "0" + hour1;
			}
			if (minutes < 10) {
				minu1 = "0" + minu1;
			}
			memoTime.setText(hour1 + JG + minu1);
			memoDate.setText(year + "/" + mouth1 + "/" + day1);
		}
		// RelativeLayout Fontbglayout = (RelativeLayout)
		// findViewById(R.id.contentbg_layout);
		// Fontbglayout.setAlpha(80);
		Fontbg_default = (RelativeLayout) findViewById(R.id.fontbg_defultbg);
		Fontbg_1 = (RelativeLayout) findViewById(R.id.fontbg_1bg);
		Fontbg_2 = (RelativeLayout) findViewById(R.id.fontbg_2bg);
		Fontbg_3 = (RelativeLayout) findViewById(R.id.fontbg_3bg);
		Fontbg_4 = (RelativeLayout) findViewById(R.id.fontbg_4bg);
		Fontbg_5 = (RelativeLayout) findViewById(R.id.fontbg_5bg);
		Fontbg_6 = (RelativeLayout) findViewById(R.id.fontbg_6bg);
		Fontbg_7 = (RelativeLayout) findViewById(R.id.fontbg_7bg);
		Fontbg_default.setOnClickListener(this);
		Fontbg_1.setOnClickListener(this);
		Fontbg_2.setOnClickListener(this);
		Fontbg_3.setOnClickListener(this);
		Fontbg_4.setOnClickListener(this);
		Fontbg_5.setOnClickListener(this);
		Fontbg_6.setOnClickListener(this);
		Fontbg_7.setOnClickListener(this);
		// initFontBG();
	}

	private void sendMemoToserver(Boolean ispublic) {

		mHttpHandler = new HttpHandler(NewMemoActivity.this);
		JSONObject JsonPageNo = new JSONObject();
		try {
			String[] date = memoDate.getText().toString().split(",");
			String datestr = "";
			for (String mdate : date) {
				if (datestr.length() > 0) {
					datestr = datestr + "," + mdate + " "
							+ memoTime.getText().toString();
				} else {
					datestr = mdate + " " + memoTime.getText().toString();
				}

			}
			JsonPageNo.put("type", 1);
			JsonPageNo.put("content", memocontent.getText().toString().trim());
			JsonPageNo.put("taskTimes", datestr);
			int isalarmclcok = memoAlamClock.isChecked() ? 1 : 2;
			JsonPageNo.put("ifAlarm", isalarmclcok);
			JsonPageNo.put("advancedTime", 0);
			JsonPageNo.put("toPhoneNum", inputFriendPhone.getText().toString());
			if (ispublic) {
				JsonPageNo.put("x", coordistaX);
				JsonPageNo.put("y", coordistaY);
				JsonPageNo.put("fromAddr", cityTag);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mHttpHandler.addJSONObject(JsonPageNo);
		if (ispublic) {
			mHttpHandler.startHandler(HttpUrlList.httpaddCommonMemoURl);
		} else {
			mHttpHandler.startHandler(HttpUrlList.httpaddMemoURl);
		}

		mHttpHandler.setOnHttpRequestListener(new OnHttpRequestListener() {

			@Override
			public void startHttpRequest(String message) {
				// TODO Auto-generated method stub
				mToastLoding = new ToastLoding(NewMemoActivity.this, "发送中");
				mToastLoding.show();
			}

			@Override
			public void onHttpRequestDone(String data) {
				// TODO Auto-generated method stub
				mToastLoding.cancel();
				JsonResult(data);
			}

			@Override
			public void onError(String message) {
				// TODO Auto-generated method stub
				mToastLoding.cancel();
				new DialogUtil(NewMemoActivity.this, message).show();
			}
		});
	}

	private static final int BACA_PAGE_ID = 1;
	Handler mhandler = new Handler() {

		@SuppressLint("HandlerLeak")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case BACA_PAGE_ID:
				setResult(111);
				finish();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	private void JsonResult(String con) {
		// TODO Auto-generated method stub
		try {
			JSONObject all = new JSONObject(con);
			Log.d(TAG + "JsongetVcodeResult", con);
			Boolean result = all.optBoolean("result");
			int resultId = all.optInt("resultId");
			String resultMSG = all.optString("resultMSG");
			if (result) {
				GetServerMemo.startGet(this, 1, 50,
						MyApplication.getMyApplication().mNowTime.NowDate,
						true, false);
				ToastDialog ta = new ToastDialog(this, resultMSG);
				ta.show();
				ta.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						setResult(111);
						finish();
					}
				});

			} else {
				if (resultId == 5006) {
					new ToastDialog(this, resultMSG).show();
					mhandler.sendEmptyMessageDelayed(BACA_PAGE_ID, 2100);
				} else {
					APPConfigure.CheckToken(this, resultId, resultMSG);
				}

			}

			all = null;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addmemo() {
		mMemoInfoService = new MemoInfoService(this);
		String content = memocontent.getText().toString();
		if (content.length() <= 0) {
			new DialogUtil(NewMemoActivity.this, "请输入备忘内容！").show();
			return;
		}

		String time = memoTime.getText().toString();
		int type = 1;
		int isalarmclcok = memoAlamClock.isChecked() ? 1 : 0;

		String[] date = memoDate.getText().toString().split(",");
		if (date == null | date.length <= 1) {
			MemoInfo memoInfo = new MemoInfo(content, time, memoDate.getText()
					.toString(), type, isalarmclcok, nowBgcorlor);

			String id = String.valueOf(mMemoInfoService.save(memoInfo));
			if (isalarmclcok == 1) {
				setAlarmClock(Integer.parseInt(id), year, mouth, day, hour,
						minutes, content);
			}

		} else {
			for (String mdate : date) {
				MemoInfo memoInfo = new MemoInfo(content, time, mdate, type,
						isalarmclcok, nowBgcorlor);
				String id = String.valueOf(mMemoInfoService.save(memoInfo));
				if (isalarmclcok == 1) {

					int year = this.year;
					int mouth = this.mouth;
					int day = this.day;
					String[] datestr = mdate.split("/");
					Toast.makeText(this, datestr.toString(), Toast.LENGTH_SHORT)
							.show();
					if (datestr != null && datestr.length == 3) {
						year = Integer.parseInt(datestr[0]);
						mouth = Integer.parseInt(datestr[1]);
						day = Integer.parseInt(datestr[2]);
					}
					setAlarmClock(Integer.parseInt(id), year, mouth, day, hour,
							minutes, content);
				}
			}

		}

		setResult(111);
		finish();
	}

	private void editMemo() {
		mMemoInfoService = new MemoInfoService(this);
		String content = memocontent.getText().toString();
		if (content.length() <= 0) {
			new DialogUtil(NewMemoActivity.this, "请输入备忘内容！").show();
			return;
		}
		int id = MainActivity.getMainActivity().childList.get(groupid).get(
				childid).id;
		String time = memoTime.getText().toString();
		int type = 1;
		int isalarmclcok = memoAlamClock.isChecked() ? 1 : 0;

		String[] date = memoDate.getText().toString().split(",");
		if (date == null | date.length <= 1) {
			MemoInfo memoInfo = new MemoInfo(id, content, time, memoDate
					.getText().toString(), type, isalarmclcok, nowBgcorlor);

			mMemoInfoService.update(memoInfo);
			if (isalarmclcok == 1) {
				setAlarmClock(id, year, mouth, day, hour, minutes, content);
			}

		} else {
			for (int i = 0; i < date.length; i++) {
				if (i == 0) {
					MemoInfo memoInfo = new MemoInfo(id, content, time,
							date[i], type, isalarmclcok, nowBgcorlor);

					String id1 = String
							.valueOf(mMemoInfoService.save(memoInfo));
					if (isalarmclcok == 1) {
						setAlarmClock(Integer.parseInt(id1), year, mouth, day,
								hour, minutes, content);
					}
				} else {
					MemoInfo memoInfo = new MemoInfo(content, time, date[i],
							type, isalarmclcok, nowBgcorlor);

					String id1 = String
							.valueOf(mMemoInfoService.save(memoInfo));
					if (isalarmclcok == 1) {

						int year = this.year;
						int mouth = this.mouth;
						int day = this.day;
						String[] datestr = date[i].split("/");
						Toast.makeText(this, datestr.toString(),
								Toast.LENGTH_SHORT).show();
						if (datestr != null && datestr.length == 3) {
							year = Integer.parseInt(datestr[0]);
							mouth = Integer.parseInt(datestr[1]);
							day = Integer.parseInt(datestr[2]);
						}
						setAlarmClock(Integer.parseInt(id1), year, mouth, day,
								hour, minutes, content);
					}
				}

			}

		}
		setResult(111);
		finish();
	}

	@SuppressLint("NewApi")
	@TargetApi(5)
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		setResult(50);
		finish();
		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_new_memo, menu);
		return true;
	}

	private void setAlarmClock(int id, int year, int mouth, int day,
			int hourOfDay, int minute, String content) {
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
		Intent intent = new Intent(NewMemoActivity.this, AlarmReceiver.class); // 创建Intent对象
		intent.putExtra("content", content);
		PendingIntent pi = PendingIntent.getBroadcast(NewMemoActivity.this, id,
				intent, 0); // 创建PendingIntent
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
		new DialogUtil(NewMemoActivity.this, "设置便签成功! " + c.get(Calendar.YEAR)
				+ "/" + (c.get(Calendar.MONTH) + 1) + "/"
				+ c.get(Calendar.DAY_OF_MONTH) + "  "
				+ c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE))
				.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode > 0) {
			switch (resultCode) {
			case 103:

				String result = data.getExtras().getString("resultDate");
				if (memoDate != null && result != null && result.length() > 0) {
					memoDate.setText(result);
				}

				break;

			default:
				break;
			}
		}
	}

	private void initFontBG() {
		switch (APPConfigure.APP_Label_nowbg) {
		case APPConfigure.APP_Label_bg_defult:
			Fontbg_default.setBackgroundResource(R.drawable.select_bg);
			Fontbg_1.setBackgroundResource(R.drawable.transparent);
			Fontbg_2.setBackgroundResource(R.drawable.transparent);
			Fontbg_3.setBackgroundResource(R.drawable.transparent);
			Fontbg_4.setBackgroundResource(R.drawable.transparent);
			Fontbg_5.setBackgroundResource(R.drawable.transparent);
			Fontbg_6.setBackgroundResource(R.drawable.transparent);
			Fontbg_7.setBackgroundResource(R.drawable.transparent);
			break;
		case APPConfigure.APP_Label_bg_1:

			Fontbg_default.setBackgroundResource(R.drawable.transparent);
			Fontbg_1.setBackgroundResource(R.drawable.select_bg);
			Fontbg_2.setBackgroundResource(R.drawable.transparent);
			Fontbg_3.setBackgroundResource(R.drawable.transparent);
			Fontbg_4.setBackgroundResource(R.drawable.transparent);
			Fontbg_5.setBackgroundResource(R.drawable.transparent);
			Fontbg_6.setBackgroundResource(R.drawable.transparent);
			Fontbg_7.setBackgroundResource(R.drawable.transparent);
			break;
		case APPConfigure.APP_Label_bg_2:

			Fontbg_default.setBackgroundResource(R.drawable.transparent);
			Fontbg_1.setBackgroundResource(R.drawable.transparent);
			Fontbg_2.setBackgroundResource(R.drawable.select_bg);
			Fontbg_3.setBackgroundResource(R.drawable.transparent);
			Fontbg_4.setBackgroundResource(R.drawable.transparent);
			Fontbg_5.setBackgroundResource(R.drawable.transparent);
			Fontbg_6.setBackgroundResource(R.drawable.transparent);
			Fontbg_7.setBackgroundResource(R.drawable.transparent);
			break;
		case APPConfigure.APP_Label_bg_3:

			Fontbg_default.setBackgroundResource(R.drawable.transparent);
			Fontbg_1.setBackgroundResource(R.drawable.transparent);
			Fontbg_2.setBackgroundResource(R.drawable.transparent);
			Fontbg_3.setBackgroundResource(R.drawable.select_bg);
			Fontbg_4.setBackgroundResource(R.drawable.transparent);
			Fontbg_5.setBackgroundResource(R.drawable.transparent);
			Fontbg_6.setBackgroundResource(R.drawable.transparent);
			Fontbg_7.setBackgroundResource(R.drawable.transparent);
			break;
		case APPConfigure.APP_Label_bg_4:

			Fontbg_default.setBackgroundResource(R.drawable.transparent);
			Fontbg_1.setBackgroundResource(R.drawable.transparent);
			Fontbg_2.setBackgroundResource(R.drawable.transparent);
			Fontbg_3.setBackgroundResource(R.drawable.transparent);
			Fontbg_4.setBackgroundResource(R.drawable.select_bg);
			Fontbg_5.setBackgroundResource(R.drawable.transparent);
			Fontbg_6.setBackgroundResource(R.drawable.transparent);
			Fontbg_7.setBackgroundResource(R.drawable.transparent);
			break;
		case APPConfigure.APP_Label_bg_5:

			Fontbg_default.setBackgroundResource(R.drawable.transparent);
			Fontbg_1.setBackgroundResource(R.drawable.transparent);
			Fontbg_2.setBackgroundResource(R.drawable.transparent);
			Fontbg_3.setBackgroundResource(R.drawable.transparent);
			Fontbg_4.setBackgroundResource(R.drawable.transparent);
			Fontbg_5.setBackgroundResource(R.drawable.select_bg);
			Fontbg_6.setBackgroundResource(R.drawable.transparent);
			Fontbg_7.setBackgroundResource(R.drawable.transparent);
			break;
		case APPConfigure.APP_Label_bg_6:

			Fontbg_default.setBackgroundResource(R.drawable.transparent);
			Fontbg_1.setBackgroundResource(R.drawable.transparent);
			Fontbg_2.setBackgroundResource(R.drawable.transparent);
			Fontbg_3.setBackgroundResource(R.drawable.transparent);
			Fontbg_4.setBackgroundResource(R.drawable.transparent);
			Fontbg_5.setBackgroundResource(R.drawable.transparent);
			Fontbg_6.setBackgroundResource(R.drawable.select_bg);
			Fontbg_7.setBackgroundResource(R.drawable.transparent);
			break;
		case APPConfigure.APP_Label_bg_7:

			Fontbg_default.setBackgroundResource(R.drawable.transparent);
			Fontbg_1.setBackgroundResource(R.drawable.transparent);
			Fontbg_2.setBackgroundResource(R.drawable.transparent);
			Fontbg_3.setBackgroundResource(R.drawable.transparent);
			Fontbg_4.setBackgroundResource(R.drawable.transparent);
			Fontbg_5.setBackgroundResource(R.drawable.transparent);
			Fontbg_6.setBackgroundResource(R.drawable.transparent);
			Fontbg_7.setBackgroundResource(R.drawable.select_bg);
			break;
		default:
			break;
		}
	}

	private void setFontBg(int id) {
		switch (id) {
		case R.id.fontbg_defultbg:
			nowBgcorlor = APPConfigure.APP_Label_bg_defult;

			Fontbg_default.setBackgroundResource(R.drawable.select_bg);
			Fontbg_1.setBackgroundResource(R.drawable.transparent);
			Fontbg_2.setBackgroundResource(R.drawable.transparent);
			Fontbg_3.setBackgroundResource(R.drawable.transparent);
			Fontbg_4.setBackgroundResource(R.drawable.transparent);
			Fontbg_5.setBackgroundResource(R.drawable.transparent);
			Fontbg_6.setBackgroundResource(R.drawable.transparent);
			Fontbg_7.setBackgroundResource(R.drawable.transparent);
			break;
		case R.id.fontbg_1bg:
			nowBgcorlor = APPConfigure.APP_Label_bg_1;

			Fontbg_default.setBackgroundResource(R.drawable.transparent);
			Fontbg_1.setBackgroundResource(R.drawable.select_bg);
			Fontbg_2.setBackgroundResource(R.drawable.transparent);
			Fontbg_3.setBackgroundResource(R.drawable.transparent);
			Fontbg_4.setBackgroundResource(R.drawable.transparent);
			Fontbg_5.setBackgroundResource(R.drawable.transparent);
			Fontbg_6.setBackgroundResource(R.drawable.transparent);
			Fontbg_7.setBackgroundResource(R.drawable.transparent);
			break;
		case R.id.fontbg_2bg:
			nowBgcorlor = APPConfigure.APP_Label_bg_2;

			Fontbg_default.setBackgroundResource(R.drawable.transparent);
			Fontbg_1.setBackgroundResource(R.drawable.transparent);
			Fontbg_2.setBackgroundResource(R.drawable.select_bg);
			Fontbg_3.setBackgroundResource(R.drawable.transparent);
			Fontbg_4.setBackgroundResource(R.drawable.transparent);
			Fontbg_5.setBackgroundResource(R.drawable.transparent);
			Fontbg_6.setBackgroundResource(R.drawable.transparent);
			Fontbg_7.setBackgroundResource(R.drawable.transparent);
			break;
		case R.id.fontbg_3bg:
			nowBgcorlor = APPConfigure.APP_Label_bg_3;

			Fontbg_default.setBackgroundResource(R.drawable.transparent);
			Fontbg_1.setBackgroundResource(R.drawable.transparent);
			Fontbg_2.setBackgroundResource(R.drawable.transparent);
			Fontbg_3.setBackgroundResource(R.drawable.select_bg);
			Fontbg_4.setBackgroundResource(R.drawable.transparent);
			Fontbg_5.setBackgroundResource(R.drawable.transparent);
			Fontbg_6.setBackgroundResource(R.drawable.transparent);
			Fontbg_7.setBackgroundResource(R.drawable.transparent);
			break;
		case R.id.fontbg_4bg:
			nowBgcorlor = APPConfigure.APP_Label_bg_4;

			Fontbg_default.setBackgroundResource(R.drawable.transparent);
			Fontbg_1.setBackgroundResource(R.drawable.transparent);
			Fontbg_2.setBackgroundResource(R.drawable.transparent);
			Fontbg_3.setBackgroundResource(R.drawable.transparent);
			Fontbg_4.setBackgroundResource(R.drawable.select_bg);
			Fontbg_5.setBackgroundResource(R.drawable.transparent);
			Fontbg_6.setBackgroundResource(R.drawable.transparent);
			Fontbg_7.setBackgroundResource(R.drawable.transparent);
			break;
		case R.id.fontbg_5bg:
			nowBgcorlor = APPConfigure.APP_Label_bg_5;

			Fontbg_default.setBackgroundResource(R.drawable.transparent);
			Fontbg_1.setBackgroundResource(R.drawable.transparent);
			Fontbg_2.setBackgroundResource(R.drawable.transparent);
			Fontbg_3.setBackgroundResource(R.drawable.transparent);
			Fontbg_4.setBackgroundResource(R.drawable.transparent);
			Fontbg_5.setBackgroundResource(R.drawable.select_bg);
			Fontbg_6.setBackgroundResource(R.drawable.transparent);
			Fontbg_7.setBackgroundResource(R.drawable.transparent);
			break;
		case R.id.fontbg_6bg:
			nowBgcorlor = APPConfigure.APP_Label_bg_6;

			Fontbg_default.setBackgroundResource(R.drawable.transparent);
			Fontbg_1.setBackgroundResource(R.drawable.transparent);
			Fontbg_2.setBackgroundResource(R.drawable.transparent);
			Fontbg_3.setBackgroundResource(R.drawable.transparent);
			Fontbg_4.setBackgroundResource(R.drawable.transparent);
			Fontbg_5.setBackgroundResource(R.drawable.transparent);
			Fontbg_6.setBackgroundResource(R.drawable.select_bg);
			Fontbg_7.setBackgroundResource(R.drawable.transparent);
			break;
		case R.id.fontbg_7bg:
			nowBgcorlor = APPConfigure.APP_Label_bg_7;

			Fontbg_default.setBackgroundResource(R.drawable.transparent);
			Fontbg_1.setBackgroundResource(R.drawable.transparent);
			Fontbg_2.setBackgroundResource(R.drawable.transparent);
			Fontbg_3.setBackgroundResource(R.drawable.transparent);
			Fontbg_4.setBackgroundResource(R.drawable.transparent);
			Fontbg_5.setBackgroundResource(R.drawable.transparent);
			Fontbg_6.setBackgroundResource(R.drawable.transparent);
			Fontbg_7.setBackgroundResource(R.drawable.select_bg);
			break;
		default:
			break;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		setFontBg(v.getId());
	}

	public static final int OPEN_CONTACTS = 23;

	private void openContacts() {
		contratsList = new ArrayList<Map<String, String>>();
		getPhoneContacts();
		getSIMContacts();
		ContartsPopupWindow MContartsPopupWindow = new ContartsPopupWindow(this);
		MContartsPopupWindow.initmPopupWindowView(
				(LinearLayout) findViewById(R.id.NewMemoActivityID),
				contratsList);
	}

	/** 得到手机通讯录联系人信息 **/
	private void getPhoneContacts() {
		ContentResolver resolver = getContentResolver();
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, new String[] {
				"display_name", "sort_key", "contact_id", "data1" }, null,
				null, "sort_key");
		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				int nameIndex = phoneCursor.getColumnIndex(Phone.DISPLAY_NAME); // 获取联系人name
				String name = phoneCursor.getString(nameIndex);
				String phoneNumber = phoneCursor.getString(phoneCursor
						.getColumnIndex(Phone.NUMBER)); // 获取联系人number
				String sortKey = getSortKey(phoneCursor.getString(1));
				if (null != name) {
					map.put("name", name);
				}
				if (null != phoneNumber) {
					map.put("phone", phoneNumber);
				}
				if (null != sortKey) {
					map.put("sortKey", sortKey);
				}
				if (map.size() > 0) {
					contratsList.add(map);
				}

			}
		}
	}

	/**
	 * 获取sort key的首个字符，如果是英文字母就直接返回，否则返回#。
	 * 
	 * @param sortKeyString
	 *            数据库中读取出的sort key
	 * @return 英文字母或者#
	 */
	private static String getSortKey(String sortKeyString) {
		if (sortKeyString.length() < 1) {
			return sortKeyString;
		}
		String key = sortKeyString.substring(0, 1).toUpperCase();
		if (key.matches("[A-Z]")) {
			return key;
		}
		return "#";
	}

	/** 得到SIM通讯录联系人信息 **/
	private void getSIMContacts() {
		ContentResolver resolver = getContentResolver();
		Uri uri = Uri.parse("content://icc/adn");
		Cursor phoneCursor = resolver.query(uri, new String[] { "display_name",
				"sort_key", "contact_id", "data1" }, null, null, "sort_key");
		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {
				String name = phoneCursor.getString(phoneCursor
						.getColumnIndex("name"));
				String phoneNumber = phoneCursor.getString(phoneCursor
						.getColumnIndex("number"));
				String sortKey = getSortKey(phoneCursor.getString(1));
				Map<String, String> map = new HashMap<String, String>();
				if (null != name) {
					map.put("name", name);
				}
				if (null != phoneNumber) {
					map.put("phone", phoneNumber);
				}
				if (null != sortKey) {
					map.put("sortKey", sortKey);
				}
				if (map.size() > 0) {
					contratsList.add(map);
				}
			}
		}

	}

	public void setContarts(ArrayList<Map<String, String>> clickList) {
		String str = "";
		if (null != clickList && clickList.size() > 0) {
			inputFriendPhone.setText("");

			for (Map<String, String> map : clickList) {
				if (clickList.size() > 1) {
					str = str + map.get("phone").trim() + ",";
				} else {
					str = map.get("phone").trim();
				}

			}

		}
		if (clickList.size() > 1) {
			str = str.substring(0, str.length() - 1);
		}
		str = str.replace(" ", "");

		inputFriendPhone.setText(str);
	}

	private double coordistaX = 0;
	private double coordistaY = 0;
	private List<String> cityTagList;
	private String cityTag = "";

	public void setCityTag(String str) {
		cityTag = str;
		new ToastDialog(this, str).show();
	}

	@Override
	public void onGaodeLocationFinish(AMapLocation location,
			List<PoiItem> mPoiItem) {
		// TODO Auto-generated method stub
		cityTagList = new ArrayList<String>();
		getGestionCities = mPoiItem;
		if (null != mToastLoding && mToastLoding.isShowing()) {
			mToastLoding.cancel();
		}
		memoIssendCommon.setChecked(true);
		memoIssendCommon.setTag("True");
		coordistaX = location.getLatitude();
		coordistaY = location.getLongitude();
		// cityTag = location.getExtras().getString("desc");
		if (null != getGestionCities && getGestionCities.size() > 0) {
			for (PoiItem tr : getGestionCities) {
				String cityTag = tr.getTitle() + "(" + tr.getSnippet() + ")";
				Log.d(TAG + "cityTag", "getCityName:" + tr.getCityName());
				Log.d(TAG + "cityTag", "getDirection:" + tr.getDirection());
				Log.d(TAG + "cityTag",
						"getProvinceName:" + tr.getProvinceName());
				Log.d(TAG + "cityTag", "getSnippet:" + tr.getSnippet());
				Log.d(TAG + "cityTag", "getTitle:" + tr.getTitle());
				Log.d(TAG + "cityTag", "getWebsite:" + tr.getWebsite());
				cityTagList.add(cityTag);
			}
		}
		LocationAddressPopupWindow locationPopupWindow = new LocationAddressPopupWindow(
				this);
		locationPopupWindow.initmPopupWindowView(
				(LinearLayout) findViewById(R.id.NewMemoActivityID),
				cityTagList);

	}

	@Override
	public void onError(String message) {
		// TODO Auto-generated method stub
		memoIssendCommon.setChecked(false);
		memoIssendCommon.setTag("False");
		Log.d("GaodeLocation-back", "onError：" + message);
	}

	@Override
	public void onShow() {
		// TODO Auto-generated method stub
		Log.d("GaodeLocation-back", "onShow：");
	}
}
