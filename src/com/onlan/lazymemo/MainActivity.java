package com.onlan.lazymemo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView.LayoutParams;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onlan.lazymemo.Http.ClickAppVersion;
import com.onlan.lazymemo.Http.GetServerMemo;
import com.onlan.lazymemo.Http.HttpHandler;
import com.onlan.lazymemo.Http.HttpHandler.OnHttpRequestListener;
import com.onlan.lazymemo.Http.HttpUrlList;
import com.onlan.lazymemo.sqlData.MemoInfoService;
import com.onlan.lazymemo.sqlData.ReadLocaMemo;
import com.onlan.mainlistViewObject.Group;
import com.onlan.mainlistViewObject.ListItem;
import com.onlan.mainlistViewObject.MainListAdapter;
import com.onlan.mainlistViewObject.PinnedHeaderExpandableListView;
import com.onlan.mainlistViewObject.PinnedHeaderExpandableListView.OnHeaderUpdateListener;
import com.onlan.myView.DateTimeComparator;
import com.onlan.myView.SpannerPopupWindow;
import com.onlan.myView.ToastDialog;
import com.onlan.myView.ToastUitil;
import com.onlna.lazymemo.ArcMenu.SatelliteMenu;
import com.onlna.lazymemo.ArcMenu.SatelliteMenu.SateliteClickedListener;
import com.onlna.lazymemo.ArcMenu.SatelliteMenuItem;

@SuppressLint("UseSparseArrays")
public class MainActivity extends Activity implements OnHeaderUpdateListener,
		OnChildClickListener, OnGroupClickListener {
	private static final int[] ITEM_DRAWABLES = { R.drawable.menu_now,
			R.drawable.menu_history, R.drawable.menu_send,
			R.drawable.menu_public, R.drawable.menu_refresh };
	private String TAG = "MainActivity";
	private LinearLayout mTitlelayout;
	private SpannerPopupWindow mSpannerPopupWindow;
	private MainListAdapter adapter;
	private PinnedHeaderExpandableListView expandableListView;
	private ArrayList<Group> groupList;
	public ArrayList<List<ListItem>> childList;
	private static MainActivity mMainActivity;
	private ArrayList<Group> historygroupList;
	public ArrayList<List<ListItem>> historygchildList;
	private static MemoInfoService mMemoInfoService;
	private boolean isHistory = false;
	private TextView possionTV;
	private ReadLocaMemo mReadLocaMemo;
	private GetServerMemo mGetServerMemo;
	private List<Integer> addlistInteger;
	private List<ListItem> addlistListItem;
	private TextView title_show;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		mMainActivity = this;
		MyApplication.addActivity(this);
		mReadLocaMemo = new ReadLocaMemo();
		mGetServerMemo = new GetServerMemo();
		init();
		addnowData();
		APPConfigure.setAPPBackGroup(null,
				(RelativeLayout) findViewById(R.id.MainActivityID),
				APPConfigure.APPBgImgUrl, APPConfigure.APPBgImgID);
		ClickAppVersion.startClickAppVersion(MainActivity.this, 2);
		initMenu();
	}

	public static MainActivity getMainActivity() {
		return mMainActivity;
	}

	private void init() {
		mMemoInfoService = new MemoInfoService(this);
		mTitlelayout = (LinearLayout) findViewById(R.id.title_layout);
		final ImageView forword = (ImageView) findViewById(R.id.title_img);
		final ImageView setting = (ImageView) findViewById(R.id.left_setting);
		possionTV = (TextView) findViewById(R.id.left_possion);
		title_show = (TextView) findViewById(R.id.title_tv);
		title_show.setText("便签墙");
		setting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent addmemo = new Intent();
				addmemo.setClass(MainActivity.this, SettingActivity.class);
				startActivityForResult(addmemo, 111);
			}
		});
		mTitlelayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mSpannerPopupWindow = new SpannerPopupWindow(MainActivity.this);
				mSpannerPopupWindow.initmPopupWindowView(mTitlelayout);
				forword.setImageResource(R.drawable.icon_down);
			}
		});
		ImageView newmemo = (ImageView) findViewById(R.id.right_add);
		newmemo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent addmemo = new Intent();
				addmemo.setClass(MainActivity.this, NewMemoActivity.class);
				addmemo.putExtra("isedit", "false");
				startActivityForResult(addmemo, 101);
			}
		});
		expandableListView = (PinnedHeaderExpandableListView) findViewById(R.id.expandablelist);
		if (APPConfigure.UserIsLogin) {
			possionTV.setVisibility(View.GONE);
		}
	}

	private void initMenu() {
		List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();

		items.add(new SatelliteMenuItem(5, ITEM_DRAWABLES[4]));// 刷新
		items.add(new SatelliteMenuItem(4, ITEM_DRAWABLES[3]));// 公共便签
		items.add(new SatelliteMenuItem(3, ITEM_DRAWABLES[2]));// 发送便签
		items.add(new SatelliteMenuItem(2, ITEM_DRAWABLES[1]));// 历史便签
		items.add(new SatelliteMenuItem(1, ITEM_DRAWABLES[0]));// 当前便签
		SatelliteMenu menu = (SatelliteMenu) findViewById(R.id.menu);
		menu.addItems(items);

		menu.setOnItemClickedListener(new SateliteClickedListener() {

			public void eventOccured(int id) {
				Log.i("sat", "Clicked on " + id);
				switch (id) {
				case 1:// 当前便签
					initNowMemo(true);
					break;
				case 2:// 历史便签
					initHisMemo(true);
					break;
				case 3:// 发送便签
					goSendMemo();
					break;
				case 4:// 公共便签
					gopublicMemo();
					break;
				case 5:// 刷新
					if (isHistory) {
						initHisMemo(true);
					} else {
						initNowMemo(true);
					}

					break;

				default:
					break;
				}
			}
		});
	}

	private Boolean isaddGroup(String date) {
		Boolean adddate = true;
		if (groupList.size() > 0) {
			for (Group group : groupList) {
				Log.d(TAG, "group:" + group.getTitle() + "--date:" + date);
				if (date.trim().equals(group.getTitle().trim())) {
					Log.d(TAG, "相等");
					return false;
				} else {
					Log.d(TAG, "不相等");
					adddate = true;
				}
			}
		} else {
			adddate = true;
		}
		return adddate;
	}

	private Boolean ishisaddGroup(String date) {
		Boolean adddate = true;
		if (historygroupList.size() > 0) {
			for (Group group : historygroupList) {
				if (date.trim().equals(group.getTitle().trim())) {
					return false;
				} else {
					adddate = true;
				}
			}
		} else {
			adddate = true;
		}
		return adddate;
	}

	public void addHistoryMemo() {
		// 展开所有group
		isHistory = true;
		if (historygroupList != null) {
			historygroupList = null;
		}
		if (historygchildList != null) {

			historygchildList = null;
		}

		historygroupList = mReadLocaMemo.getHistorygroupList();
		historygchildList = mReadLocaMemo.getHistorygchildList();

		if (historygroupList != null && historygroupList.size() > 0) {
			if (mGetServerMemo.getHistorygroupList() != null
					&& mGetServerMemo.getHistorygroupList().size() > 0) {
				for (Group mgroup : mGetServerMemo.getHistorygroupList()) {
					if (ishisaddGroup(mgroup.getTitle().trim())) {
						historygroupList.add(mgroup);
					}
				}
			}
		} else {
			historygroupList = mGetServerMemo.getHistorygroupList();
		}
		ArrayList<List<ListItem>> List = new ArrayList<List<ListItem>>();
		if (historygchildList != null && historygchildList.size() > 0) {
			if (mGetServerMemo.getHistorygchildList() != null
					&& mGetServerMemo.getHistorygchildList().size() > 0) {
				for (List<ListItem> ser : mGetServerMemo.getHistorygchildList()) {
					for (ListItem serListItem : ser) {
						for (int y = 0; y < historygchildList.size(); y++) {
							Boolean isadd = false;
							for (ListItem listItem : historygchildList.get(y)) {
								String date1 = listItem.date.trim();
								String date2 = serListItem.date.trim();
								if (date1.equals(date2)) {
									isadd = true;
								}
							}
							if (isadd) {
								historygchildList.get(y).add(serListItem);
							} else {
								List.add(ser);
							}
						}
					}
				}
			}
		} else {
			historygchildList = mGetServerMemo.getHistorygchildList();
		}
		if (null != historygroupList && null != historygchildList) {
			historygchildList.addAll(List);
			DateTimeComparator.comparatorbeforeDate(historygroupList);
			DateTimeComparator.comparatorDateAndTime(historygchildList);
			adapter = new MainListAdapter(this, historygroupList,
					historygchildList);
			expandableListView.setAdapter(adapter);
			Log.d(TAG,
					"expandableListView.getCount()"
							+ expandableListView.getCount());
			for (int i = 0, count = expandableListView.getCount(); i < count; i++) {
				expandableListView.expandGroup(i);
			}
			expandableListView.setOnHeaderUpdateListener(this);
			expandableListView.setOnChildClickListener(this);
			expandableListView.setOnGroupClickListener(this, false);
			adapter.notifyDataSetChanged();
		}
	}

	public void addnowData() {
		isHistory = false;
		// 展开所有group
		if (groupList != null) {
			groupList = null;
		}
		if (childList != null) {
			childList = null;
		}
		groupList = mReadLocaMemo.getNowgroupList();
		childList = mReadLocaMemo.getNowchildList();

		if (groupList != null && groupList.size() > 0) {

			if (mGetServerMemo.getNowgroupList() != null
					&& mGetServerMemo.getNowgroupList().size() > 0) {
				for (Group mgroup : mGetServerMemo.getNowgroupList()) {
					if (isaddGroup(mgroup.getTitle().trim())) {
						Log.d(TAG + "", "mgroup:" + mgroup.getTitle().trim());
						groupList.add(mgroup);
					}
				}
			}
		} else {
			groupList = mGetServerMemo.getNowgroupList();
		}
		addlistInteger = new ArrayList<Integer>();
		addlistListItem = new ArrayList<ListItem>();
		ArrayList<List<ListItem>> List = new ArrayList<List<ListItem>>();
		if (childList != null && childList.size() > 0) {
			if (mGetServerMemo.getNowchildList() != null
					&& mGetServerMemo.getNowchildList().size() > 0) {
				for (List<ListItem> ser : mGetServerMemo.getNowchildList()) {
					Boolean isADD = true;
					for (ListItem serListItem : ser) {

						for (int y = 0; y < childList.size(); y++) {
							Boolean isaddnow = false;
							for (ListItem listItem : childList.get(y)) {
								String date1 = listItem.date.trim();
								String date2 = serListItem.date.trim();
								Log.d(TAG + "listItem", serListItem.content
										+ "--" + date1);
								if (date2.equals(date1)) {
									Log.d(TAG + "=======", serListItem.content
											+ "--" + date1);
									isADD = false;
									isaddnow = true;
								}
							}
							if (isaddnow) {
								addlistInteger.add(y);
								addlistListItem.add(serListItem);
							}
						}
					}
					for (Group mgroup : groupList) {
						if (isADD
								&& ser.get(0).date.trim().equals(
										mgroup.getTitle().trim())) {
							List.add(ser);
						}
					}
				}

			}
		} else {
			childList = mGetServerMemo.getNowchildList();
		}

		if (groupList != null && childList != null) {
			childList.addAll(List);
			for (int a = 0; a < addlistInteger.size(); a++) {
				Log.d(TAG + "addlist",
						"a" + a + "--values:" + addlistInteger.get(a));
				childList.get(addlistInteger.get(a))
						.add(addlistListItem.get(a));
			}
			DateTimeComparator.comparatorafterDate(groupList);
			DateTimeComparator.comparatorbeforeDateAndTime(childList);
			adapter = new MainListAdapter(this, groupList, childList);
			expandableListView.setAdapter(adapter);
			for (int i = 0, count = expandableListView.getCount(); i < count; i++) {
				expandableListView.expandGroup(i);
			}
			expandableListView.setOnHeaderUpdateListener(this);
			expandableListView.setOnChildClickListener(this);
			expandableListView.setOnGroupClickListener(this, false);
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	public void deleteServerMemo(ArrayList<List<ListItem>> nowList, int goupID,
			int chirdId) {
		if (!nowList.get(goupID).get(chirdId).isLocal) {
			HttpHandler mHttpHandler = new HttpHandler(this);

			JSONObject JsonPageNo = new JSONObject();
			try {

				JsonPageNo.put("memoId", nowList.get(goupID).get(chirdId).id);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mHttpHandler.addJSONObject(JsonPageNo);
			mHttpHandler.startHandler(HttpUrlList.httpDeleteMemoURl);
			mHttpHandler.setOnHttpRequestListener(new OnHttpRequestListener() {

				@Override
				public void startHttpRequest(String message) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onHttpRequestDone(String data) {
					// TODO Auto-generated method stub
					Log.d(TAG + "JsongetVcodeResult", data);
					JsonResult(data);
				}

				@Override
				public void onError(String message) {
					// TODO Auto-generated method stub
					new ToastDialog(MainActivity.this, message).show();
				}
			});
		}

	}

	private void JsonResult(String con) {
		// TODO Auto-generated method stub
		try {

			JSONObject all = new JSONObject(con);
			Log.d(TAG, con);
			// Boolean result = all.optBoolean("result");
			String resultMSG = all.optString("resultMSG");
			new ToastDialog(MainActivity.this, resultMSG).show();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		// TODO Auto-generated method stub

		Intent bigmemo = new Intent();
		bigmemo.setClass(MainActivity.this, BigMemoActivity.class);
		bigmemo.putExtra("groupid", groupPosition);
		bigmemo.putExtra("childid", childPosition);
		Log.d(TAG + "sult", "groupid:" + groupPosition + "---childid:"
				+ childPosition);
		if (!isHistory) {
			Log.d(TAG + "sult-id",
					String.valueOf(childList.get(groupPosition).get(
							childPosition).id));
			bigmemo.putExtra("isHistory", false);
			bigmemo.putExtra("time",
					childList.get(groupPosition).get(childPosition).time);
			bigmemo.putExtra("content",
					childList.get(groupPosition).get(childPosition).content);
			bigmemo.putExtra("bgcolor",
					childList.get(groupPosition).get(childPosition).bgcolor);
			bigmemo.putExtra("id",
					childList.get(groupPosition).get(childPosition).id);
			bigmemo.putExtra(
					"taskDate",
					childList.get(groupPosition).get(childPosition).date
							+ childList.get(groupPosition).get(childPosition).time);
			bigmemo.putExtra(
					"isalarmclock",
					childList.get(groupPosition).get(childPosition).isalarmclock);
			if (!childList.get(groupPosition).get(childPosition).isLocal) {
				bigmemo.putExtra("isLocal", true);
			} else {
				bigmemo.putExtra("isLocal", false);
			}
		} else {
			bigmemo.putExtra("isHistory", true);
			Log.d(TAG + "sult-id",
					String.valueOf(historygchildList.get(groupPosition).get(
							childPosition).id));
			bigmemo.putExtra("id",
					historygchildList.get(groupPosition).get(childPosition).id);
			bigmemo.putExtra("taskDate", historygchildList.get(groupPosition)
					.get(childPosition).date

			+ historygchildList.get(groupPosition).get(childPosition).time);
			bigmemo.putExtra(
					"isalarmclock",
					historygchildList.get(groupPosition).get(childPosition).isalarmclock);
			bigmemo.putExtra(
					"time",
					historygchildList.get(groupPosition).get(childPosition).time);
			bigmemo.putExtra("content", historygchildList.get(groupPosition)
					.get(childPosition).content);
			bigmemo.putExtra("bgcolor", historygchildList.get(groupPosition)
					.get(childPosition).bgcolor);
			if (!historygchildList.get(groupPosition).get(childPosition).isLocal) {
				bigmemo.putExtra("isLocal", true);
			} else {
				bigmemo.putExtra("isLocal", false);
			}
		}
		startActivityForResult(bigmemo, 10);
		return false;
	}

	@Override
	public View getPinnedHeader() {
		// TODO Auto-generated method stub
		View headerView = (ViewGroup) getLayoutInflater().inflate(
				R.layout.group, null);
		headerView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		return headerView;
	}

	public void initHisMemo(Boolean isloding) {

		GetServerMemo.startGet(this, 1, 50,
				MyApplication.getMyApplication().mNowTime.NowDate, false, true);
		mReadLocaMemo.startRead(this);
		if (!isloding) {
			clickHisData();
		}

	}

	public void clickHisData() {
		init();
		addHistoryMemo();
		adapter.notifyDataSetChanged();
		if (APPConfigure.UserIsLogin) {
			possionTV.setVisibility(View.GONE);
		}
		title_show.setText("历史便签");
	}

	public void initNowMemo(Boolean isloding) {

		GetServerMemo.startGet(this, 1, 50,
				MyApplication.getMyApplication().mNowTime.NowDate, true, true);
		mReadLocaMemo.startRead(this);
		if (!isloding) {
			clickNowData();
		}

		// title_show.setText("当前便签");
	}

	public void clickNowData() {
		init();
		addnowData();
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}

		if (APPConfigure.UserIsLogin) {
			possionTV.setVisibility(View.GONE);
		} else {
			possionTV.setVisibility(View.VISIBLE);
		}
	}

	public void goSendMemo() {
		Intent sendmemo = new Intent();
		sendmemo.setClass(MainActivity.this, MySendMemoActivity.class);
		startActivityForResult(sendmemo, 20);
	}

	public void gopublicMemo() {
		Intent sendmemo = new Intent();
		sendmemo.setClass(MainActivity.this, CommonMemoActivity.class);
		startActivityForResult(sendmemo, 20);
	}

	@Override
	public void updatePinnedHeader(View headerView, int firstVisibleGroupPos) {
		if (adapter != null && headerView != null && firstVisibleGroupPos > -1) {
			Group firstVisibleGroup = (Group) adapter
					.getGroup(firstVisibleGroupPos);
			TextView textView = (TextView) headerView.findViewById(R.id.group);
			textView.setText(firstVisibleGroup.getTitle());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode > 0) {
			switch (resultCode) {
			case 10:
				int groupid = data.getExtras().getInt("groupid");
				int childid = data.getExtras().getInt("childid");
				Log.d(TAG + "sult", "groupid:" + groupid + "---childid:"
						+ childid);
				if (!isHistory) {
					if (childList.get(groupid).get(childid).isLocal) {
						mMemoInfoService
								.deleteSignleinfo(String.valueOf(childList.get(
										groupid).get(childid).id));
					} else {
						deleteServerMemo(childList, groupid, childid);
					}

					childList.get(groupid).remove(childid);
				} else {
					if (historygchildList.get(groupid).get(childid).isLocal) {
						mMemoInfoService.deleteSignleinfo(String
								.valueOf(historygchildList.get(groupid).get(
										childid).id));
					} else {
						deleteServerMemo(historygchildList, groupid, childid);
					}

					historygchildList.get(groupid).remove(childid);

				}

				adapter.notifyDataSetChanged();
				break;
			case 111:
				initNowMemo(false);
				break;
			default:
				break;
			}
		}
	}

	private long mExitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.e("OUT", "OUT");
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {// 如果两次按键时间间隔大于2000毫秒，则不退出
				mExitTime = System.currentTimeMillis();// 更新mExitTime
				ToastUitil.make("再点击一次，继续退出。");
			} else {
				MyApplication.getMyApplication().exit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
