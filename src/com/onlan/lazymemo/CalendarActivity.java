package com.onlan.lazymemo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.onlan.lazymemo.Calendar.CalendarAdapter;
import com.onlan.myView.ToastUitil;

public class CalendarActivity extends Activity implements OnGestureListener {
	private GestureDetector gestureDetector = null;
	private CalendarAdapter calV = null;
	private GridView gridView = null;
	private TextView topText = null;
	private static int jumpMonth = 0; // 每次滑动，增加或减去一个月,默认为0（即显示当前月）
	private static int jumpYear = 0; // 滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private String currentDate = "";
	private static String week[] = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
	private List<DayClass> selectDay;

	private class DayClass {
		public int id;
		/**
		 * 阳历
		 */
		private String scheduleDay;
		/**
		 * 星期几
		 */
		private String week;
		/**
		 * 阴历
		 */
		private String LunarDay;
	}

	public CalendarActivity() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
		currentDate = sdf.format(date); // 当期日期
		year_c = Integer.parseInt(currentDate.split("-")[0]);
		month_c = Integer.parseInt(currentDate.split("-")[1]);
		day_c = Integer.parseInt(currentDate.split("-")[2]);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_calendar);
		MyApplication.addActivity(this);
		init();
		APPConfigure.setAPPBackGroup(
				(LinearLayout) findViewById(R.id.CalendarActivityID), null,APPConfigure.APPBgImgUrl,
				APPConfigure.APPBgImgID);
	}

	private void init() {
		selectDay = new ArrayList<DayClass>();

		gestureDetector = new GestureDetector(this);
		calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear,
				year_c, month_c, day_c);
		addGridView();
		gridView.setAdapter(calV);

		topText = (TextView) findViewById(R.id.tv_month);
		addTextToTopTextView(topText);
		ImageView back = (ImageView) findViewById(R.id.left_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		LinearLayout prev_month = (LinearLayout) findViewById(R.id.btn_prev_month);
		prev_month.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addGridView(); // 添加一个gridView
				jumpMonth--; // 上一个月
				calV = new CalendarAdapter(CalendarActivity.this,
						getResources(), jumpMonth, jumpYear, year_c, month_c,
						day_c);
				gridView.setAdapter(calV);
				addTextToTopTextView(topText);

			}
		});

		LinearLayout next_month = (LinearLayout) findViewById(R.id.btn_next_month);
		next_month.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addGridView(); // 添加一个gridView
				jumpMonth++; // 下一个月
				calV = new CalendarAdapter(CalendarActivity.this,
						getResources(), jumpMonth, jumpYear, year_c, month_c,
						day_c);
				gridView.setAdapter(calV);
				addTextToTopTextView(topText);
			}
		});
		TextView commit = (TextView) findViewById(R.id.btn_commit);
		commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent backintent = new Intent();
				backintent.putExtra("resultDate", getSelectday());
				setResult(103, backintent);
				finish();

			}
		});
	}

	private String getSelectday() {
		String str = "";
		for (DayClass mDayC : selectDay) {
			if (str.length() > 0) {
				str = str + "," + mDayC.scheduleDay;
			} else {
				str = mDayC.scheduleDay;
			}

		}
		return str;
	}

	// 添加头部的年份 闰哪月等信息
	public void addTextToTopTextView(TextView view) {
		StringBuffer textDate = new StringBuffer();
		textDate.append(calV.getShowYear()).append("年")
				.append(calV.getShowMonth()).append("月").append("\t");
		view.setText(textDate);
		view.setTextColor(Color.WHITE);
		view.setTypeface(Typeface.DEFAULT_BOLD);
	}

	// 添加gridview
	private void addGridView() {

		gridView = (GridView) findViewById(R.id.gridview);

		gridView.setOnTouchListener(new OnTouchListener() {
			// 将gridview中的触摸事件回传给gestureDetector
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return CalendarActivity.this.gestureDetector
						.onTouchEvent(event);
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {
			// gridView中的每一个item的点击事件

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
				if (position < calV.getCurrentFlag()) {
					ToastUitil.make("不能备忘过去的日期。");
					return;
				}
				DayClass mDayClass = new DayClass();
				int startPosition = calV.getStartPositon();
				int endPosition = calV.getEndPosition();
				if (startPosition <= position + 7
						&& position <= endPosition - 7) {
					String scheduleDay = calV.getDateByClickItem(position)
							.split("\\.")[0]; // 这一天的阳历
					String scheduleLunarDay = calV.getDateByClickItem(position)
							.split("\\.")[1];
					// //这一天的阴历
					String scheduleYear = calV.getShowYear();
					String scheduleMonth = calV.getShowMonth();
					Boolean isADD = true;
					List<DayClass> ddc = new ArrayList<DayClass>();
					for (DayClass dc : selectDay) {
						if (dc.id == position) {
							arg0.getChildAt(dc.id).setBackgroundColor(
									0xffffffff);
							ddc.add(dc);
							isADD = false;
						}
					}

					if (isADD) {
						int month = Integer.parseInt(scheduleMonth);
						if (month < 10) {
							scheduleMonth = "0" + scheduleMonth;
						}
						int day = Integer.parseInt(scheduleDay);
						if (day < 10) {
							scheduleDay = "0" + scheduleDay;
						}
						String daystr = scheduleYear + "/" + scheduleMonth
								+ "/" + scheduleDay;
						mDayClass.id = position;
						mDayClass.scheduleDay = daystr;
						selectDay.add(mDayClass);
						arg1.setBackgroundColor(0xff8d8d8d);
					} else {
						selectDay.removeAll(ddc);
					}

				}
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_calendar, menu);
		return true;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		int gvFlag = 0; // 每次添加gridview到viewflipper中时给的标记
		if (e1.getX() - e2.getX() > 120) {
			// 像左滑动
			addGridView(); // 添加一个gridView
			jumpMonth++; // 下一个月

			calV = new CalendarAdapter(this, getResources(), jumpMonth,
					jumpYear, year_c, month_c, day_c);
			gridView.setAdapter(calV);
			addTextToTopTextView(topText);
			gvFlag++;

			return true;
		} else if (e1.getX() - e2.getX() < -120) {
			// 向右滑动
			addGridView(); // 添加一个gridView
			jumpMonth--; // 上一个月

			calV = new CalendarAdapter(this, getResources(), jumpMonth,
					jumpYear, year_c, month_c, day_c);
			gridView.setAdapter(calV);
			gvFlag++;
			addTextToTopTextView(topText);

			return true;
		}
		return false;

	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}
