package com.onlan.lazymemo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.onlan.lazymemo.Http.HttpHandler;
import com.onlan.lazymemo.Http.HttpHandler.OnHttpRequestListener;
import com.onlan.lazymemo.Http.HttpUrlList;
import com.onlan.lazymemo.service.GaodeLocationService;
import com.onlan.lazymemo.service.GaodeLocationService.OnGaodeLocationListener;
import com.onlan.mainlistViewObject.ListItem;
import com.onlan.mainlistViewObject.MemoBgset;
import com.onlan.myView.DialogUtil;
import com.onlan.myView.ToastDialog;
import com.onlan.myView.ToastLoding;

public class CommonMemoActivity extends Activity implements
		OnGaodeLocationListener {
	private String TAG = "MySendMemoActivity";
	private ListView memoListView;
	private ArrayList<ListItem> memolist;
	private ToastLoding mToastLoding;
	private HttpHandler mHttpHandler;
	private MemoListViewAdapter mMemoListViewAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_common_memo);
		MyApplication.addActivity(this);
		init();
		mHttpHandler = new HttpHandler(this);
		APPConfigure.setAPPBackGroup(
				(LinearLayout) findViewById(R.id.CommonMemoActivityID), null,
				APPConfigure.APPBgImgUrl, APPConfigure.APPBgImgID);
		GaodeLocationService.setOnGaodeLocationListener(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mToastLoding = new ToastLoding(CommonMemoActivity.this, "加载中");
		mToastLoding.show();
		GaodeLocationService.setIsPoiAroundSearch(false);
		MyApplication.getMyApplication().openLocation();
	}

	private void init() {
		ImageView back = (ImageView) findViewById(R.id.left_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		memoListView = (ListView) findViewById(R.id.common_memolist);
		memolist = new ArrayList<ListItem>();
		mMemoListViewAdapter = new MemoListViewAdapter(memolist);
		memoListView.setAdapter(mMemoListViewAdapter);
		memoListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (arg2 < memolist.size()) {
					// showResond(memolist.get(arg2).id);
				}
			}
		});
	}

	private void addMymemoData(double coordinateX, double coordinateY) {
		JSONObject JsonPageNo = new JSONObject();

		try {
			JsonPageNo.put("x", coordinateX);
			JsonPageNo.put("y", coordinateY);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mHttpHandler.addJSONObject(JsonPageNo);
		mHttpHandler.startHandler(HttpUrlList.httpCommonMemoURl);
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
				if (mToastLoding != null && mToastLoding.isShowing()) {
					mToastLoding.cancel();
				}
				new ToastDialog(CommonMemoActivity.this, message).show();
			}
		});
	}

	private void JsonResult(String con) {
		// TODO Auto-generated method stub
		try {
			memolist.clear();
			JSONObject all = new JSONObject(con);
			Log.d(TAG + "JsonResult", con);
			Boolean result = all.optBoolean("result");
			int resultId = all.optInt("resultId");
			String resultMSG = all.optString("resultMSG");
			if (mToastLoding != null && mToastLoding.isShowing()) {
				mToastLoding.cancel();
			}
			if (result) {
				JSONObject data = all.optJSONObject("data");
				JSONArray memo = data.optJSONArray("memos");
				if (null != memo) {
					for (int i = 0; i < memo.length(); i++) {
						Log.d(TAG + "memo", i + "");
						JSONObject item = memo.optJSONObject(i);
						ListItem mlistitem = new ListItem();
						mlistitem.content = item.optString("content");
						mlistitem.id = item.optInt("id");
						mlistitem.isalarmclock = item.optInt("ifAlarm");
						mlistitem.type = item.optInt("type");
						mlistitem.date = item.optString("taskTime");
						mlistitem.bgcolor = -1;
						mlistitem.isLocal = false;
						mlistitem.FormPhone = item.optString("fromPhone");
						memolist.add(mlistitem);
					}
				}
				mMemoListViewAdapter.notifyDataSetChanged();
				StepChildComparator comparator = new StepChildComparator();
				Collections.sort(memolist, comparator);
			} else {
				APPConfigure.CheckToken(this, resultId, resultMSG);
			}
			all = null;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class MemoListViewAdapter extends BaseAdapter {
		private ArrayList<ListItem> list;
		private LayoutInflater mInflater;

		public MemoListViewAdapter(ArrayList<ListItem> mlist) {
			this.list = mlist;
			mInflater = LayoutInflater.from(CommonMemoActivity.this);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ChildHolder childHolder = null;
			if (convertView == null) {
				childHolder = new ChildHolder();
				convertView = mInflater.inflate(R.layout.mianlist_item_layout,
						null);

				childHolder.time = (TextView) convertView
						.findViewById(R.id.time);
				childHolder.content = (TextView) convertView
						.findViewById(R.id.content);
				childHolder.img = (ImageView) convertView
						.findViewById(R.id.imageView1);
				childHolder.img_juan = (ImageView) convertView
						.findViewById(R.id.imageView2);
				childHolder.contentView = (RelativeLayout) convertView
						.findViewById(R.id.main_itemview1212);
				childHolder.fontView = (RelativeLayout) convertView
						.findViewById(R.id.main_itemview);
				convertView.setTag(childHolder);
			} else {
				childHolder = (ChildHolder) convertView.getTag();
			}
			RelativeLayout.LayoutParams rl;
			if (APPConfigure.Screen_Width > 720) {
				rl = new RelativeLayout.LayoutParams(80, 80);
			} else {
				rl = new RelativeLayout.LayoutParams(50, 50);
			}
			RelativeLayout.LayoutParams rlweek = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			RelativeLayout.LayoutParams rlcontentView = new RelativeLayout.LayoutParams(
					APPConfigure.Screen_Width * 3 / 4,
					LayoutParams.WRAP_CONTENT);

			rl.addRule(RelativeLayout.ALIGN_PARENT_TOP);

			childHolder.content.setText(list.get(position).content.trim());
			if (!list.get(position).isLocal) {
				String phone = "        来自:" + list.get(position).FormPhone;

				childHolder.time.setText(list.get(position).date + phone);

				if (position % 2 == 0) {
					rl.leftMargin = 10;
					rl.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					rlcontentView.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					rlweek.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					rlweek.leftMargin = 10;
					childHolder.content.setGravity(Gravity.LEFT);
					rlweek.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
					MemoBgset.setBg(childHolder.fontView,
							list.get(position).bgcolor, childHolder.img_juan,
							true);
				} else {
					rl.rightMargin = 0;
					rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					rlcontentView.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					rlweek.rightMargin = 10;
					rlweek.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					rlweek.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
					childHolder.content.setGravity(Gravity.RIGHT);
					MemoBgset.setBg(childHolder.fontView,
							list.get(position).bgcolor, childHolder.img_juan,
							false);
				}
			}
			childHolder.img.setLayoutParams(rl);
			childHolder.time.setLayoutParams(rlweek);
			// childHolder.contentView.setLayoutParams(rlcontentView);
			childHolder.content.setTextSize(APPConfigure.APP_Memo_FontSize);
			return convertView;
		}

		class ChildHolder {
			TextView time;
			ImageView img;
			ImageView img_juan;
			TextView content;
			RelativeLayout contentView;
			RelativeLayout fontView;
		}
	}

	class StepChildComparator implements Comparator<ListItem> {
		private String DateTimeFormatPattern = "yyyy/MM/dd HH:mm";

		/**
		 * 如果o1小于o2,返回一个负数;如果o1大于o2，返回一个正数;如果他们相等，则返回0;
		 */
		@Override
		public int compare(ListItem o1, ListItem o2) {
			SimpleDateFormat format = new SimpleDateFormat(
					DateTimeFormatPattern);
			Date acceptTime1 = null;
			Date acceptTime2 = null;
			try {
				acceptTime1 = format.parse(o1.date.trim());
				acceptTime2 = format.parse(o2.date.trim());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 对日期字段进行升序，如果欲降序可采用before方法
			if (acceptTime1.before(acceptTime2)) {
				return 1;
			}
			return -1;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_common_memo, menu);
		return true;
	}

	@Override
	public void onGaodeLocationFinish(AMapLocation location,
			List<PoiItem> mPoiItem) {
		// TODO Auto-generated method stub
		double coordistaX = location.getLatitude();
		double coordistaY = location.getLongitude();
		addMymemoData(coordistaX, coordistaY);
	}

	@Override
	public void onError(String message) {
		// TODO Auto-generated method stub
		if (mToastLoding != null && mToastLoding.isShowing()) {
			mToastLoding.cancel();
		}
		new DialogUtil(this, message).show();

	}

	@Override
	public void onShow() {
		// TODO Auto-generated method stub

	}
}
