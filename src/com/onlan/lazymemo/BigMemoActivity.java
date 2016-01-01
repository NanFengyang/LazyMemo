package com.onlan.lazymemo;

import java.util.Calendar;
import java.util.Collections;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.onlan.lazymemo.Http.HttpHandler;
import com.onlan.lazymemo.Http.HttpUrlList;
import com.onlan.lazymemo.Http.HttpHandler.OnHttpRequestListener;
import com.onlan.lazymemo.MySendMemoActivity.StepChildComparator;
import com.onlan.lazymemo.service.AlarmReceiver;
import com.onlan.mainlistViewObject.ListItem;
import com.onlan.mainlistViewObject.MemoBgset;
import com.onlan.myView.DialogUtil;
import com.onlan.myView.ToastDialog;
import com.onlan.myView.ToastLoding;
import com.onlan.myView.ToastUitil;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BigMemoActivity extends Activity {
	private String TAG = "BigMemoActivity";
	private RelativeLayout bglayout;
	private int groupid;
	private int childid;
	private Boolean isHistory;
	private int bgcolor;
	private TextView time;
	private ImageView juan;
	private int MemoId;
	private HttpHandler mHttpHandler;
	private String taskDate;
	private int isalarmclock;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_big_memo);
		MyApplication.addActivity(this);
		groupid = this.getIntent().getExtras().getInt("groupid");
		childid = this.getIntent().getExtras().getInt("childid");
		bgcolor = this.getIntent().getExtras().getInt("bgcolor");
		isHistory = this.getIntent().getExtras().getBoolean("isHistory");
		MemoId = this.getIntent().getExtras().getInt("id");
		isalarmclock = this.getIntent().getExtras().getInt("isalarmclock");
		taskDate = this.getIntent().getExtras().getString("taskDate");
		mHttpHandler = new HttpHandler(this);
		init(this.getIntent().getExtras().getString("time"), this.getIntent()
				.getExtras().getString("content"));
	}

	private void init(final String strtime, final String strcontent) {
		bglayout = (RelativeLayout) findViewById(R.id.bigmemo_layout);
		ImageView back = (ImageView) findViewById(R.id.back);
		ImageView img = (ImageView) findViewById(R.id.imageView1);

		juan = (ImageView) findViewById(R.id.imageView2);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setResult(-1);
				finish();
			}
		});
		ImageView more = (ImageView) findViewById(R.id.more);
		more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent addmemo = new Intent();
				addmemo.setClass(BigMemoActivity.this, NewMemoActivity.class);
				addmemo.putExtra("isedit", "true");
				addmemo.putExtra("isHistory", isHistory);
				addmemo.putExtra("groupid", groupid);
				addmemo.putExtra("childid", childid);
				startActivityForResult(addmemo, 10);
			}
		});
		ImageView delete = (ImageView) findViewById(R.id.delete);
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent bigmemo = new Intent();
				bigmemo.putExtra("groupid", groupid);
				bigmemo.putExtra("childid", childid);
				setResult(10, bigmemo);
				finish();
			}
		});
		TextView time = (TextView) findViewById(R.id.time);
		time.setText(strtime);
		final EditText content = (EditText) findViewById(R.id.content);
		content.setEnabled(false);
		content.setText(strcontent);
		bglayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		ImageView rejuct = (ImageView) findViewById(R.id.reject);
		rejuct.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RespondMemo(0);
			}
		});
		ImageView accept = (ImageView) findViewById(R.id.accept);
		accept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RespondMemo(1);
				ToastUitil.make("isalarmclock:" + isalarmclock);
				if (isalarmclock == 1) {
					String[] date = taskDate.trim()
							.substring(0, taskDate.length() - 6).split("/");
					int year = 0;
					int mouth = 0;
					int day = 0;
					if (date != null && date.length == 3) {
						year = Integer.parseInt(date[0]);
						mouth = Integer.parseInt(date[1]);
						day = Integer.parseInt(date[2]);
					}
					String[] time = taskDate
							.trim()
							.substring(taskDate.length() - 5, taskDate.length())
							.trim().split(":");
					int hour = 0;
					int mini = 0;
					if (time != null && time.length == 2) {
						hour = Integer.parseInt(time[0]);
						mini = Integer.parseInt(time[1]);
					}
					ToastUitil.make("year:" + year + "mouth:" + mouth + "day:"
							+ day + "hour:" + hour + "mini:" + mini);

					setAlarmClock(MemoId, year, mouth, day, hour, mini, content
							.getText().toString());

				}
			}
		});
		Boolean islocal = this.getIntent().getExtras().getBoolean("isLocal");
		MemoBgset.setBg(bglayout, bgcolor, juan, islocal);
		RelativeLayout.LayoutParams rlweek = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (!islocal) {

			rlweek.rightMargin = 10;
			rlweek.bottomMargin = 5;
			rejuct.setVisibility(View.GONE);
			accept.setVisibility(View.GONE);
			rlweek.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			rlweek.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		} else {
			more.setVisibility(View.GONE);
			rlweek.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			rlweek.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			rlweek.leftMargin = 10;
			rlweek.bottomMargin = 5;
		}
		time.setLayoutParams(rlweek);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_big_memo, menu);
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		finish();
		return super.onTouchEvent(event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode > 0) {
			switch (resultCode) {
			case 50:
				this.finish();
				break;
			case 111:
				setResult(111);
				this.finish();
				break;
			default:
				break;
			}
		}
	}

	private void RespondMemo(int ifAccept) {
		JSONObject JsonPageNo = new JSONObject();
		try {
			JsonPageNo.put("memoId", MemoId);
			JsonPageNo.put("ifAccept", ifAccept);
			JsonPageNo.put("reason", "");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mHttpHandler.addJSONObject(JsonPageNo);
		mHttpHandler.startHandler(HttpUrlList.httpRespondMemoURl);
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
				new ToastDialog(BigMemoActivity.this, message).show();
			}
		});
	}

	private void JsonResult(String con) {
		// TODO Auto-generated method stub
		try {
			JSONObject all = new JSONObject(con);
			Log.d(TAG + "JsonResult", con);
			Boolean result = all.optBoolean("result");
			int resultId = all.optInt("resultId");
			String resultMSG = all.optString("resultMSG");
			if (result) {
				new ToastDialog(BigMemoActivity.this, resultMSG).show();
			} else {
				APPConfigure.CheckToken(this, resultId, resultMSG);
			}

			all = null;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		Intent intent = new Intent(this, AlarmReceiver.class); // 创建Intent对象
		intent.putExtra("content", content);
		PendingIntent pi = PendingIntent.getBroadcast(this, id, intent, 0); // 创建PendingIntent
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);

	}
}
