package com.onlan.myView;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.onlan.lazymemo.APPConfigure;
import com.onlan.lazymemo.R;

public class RespondPopupWindow {
	private Activity mActivity;
	private PopupWindow mpopupwindow;
	private LinearLayout mTitlelayout;
	private ListView respondListView;
	private LayoutInflater mInflater;
	private ArrayList<Map<String, String>> resonpList;

	public RespondPopupWindow(Activity activity) {
		this.mActivity = activity;
		mInflater = LayoutInflater.from(activity);
	}

	public void initmPopupWindowView(final LinearLayout mTitlelayout,
			ArrayList<Map<String, String>> List) {
		this.resonpList = List;
		this.mTitlelayout = mTitlelayout;
		int w = mTitlelayout.getWidth();
		// // 获取自定义布局文件pop.xml的视图
		View customView = this.mActivity.getLayoutInflater().inflate(
				R.layout.respond_item, null, false);

		// 创建PopupWindow实例,200,150分别是宽度和高度

		mpopupwindow = new PopupWindow(customView, APPConfigure.Screen_Width,
				APPConfigure.Screen_Hight);

		// 设置动画效果 [R.style.AnimationFade 是自己事先定义好的]
		mpopupwindow.setAnimationStyle(R.style.AnimationFade);
		// 使其聚集
		mpopupwindow.setFocusable(true);
		// 设置允许在外点击消失
		mpopupwindow.setOutsideTouchable(true);
		// 刷新状态（必须刷新否则无效）
		mpopupwindow.update();
		mpopupwindow.setBackgroundDrawable(new BitmapDrawable());
		// mpopupwindow.showAsDropDown(mTitlelayout, 0, -20);
		mpopupwindow.showAtLocation(mTitlelayout, Gravity.CENTER, 0, 0);
		mpopupwindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub

			}
		});
		customView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		respondListView = (ListView) customView.findViewById(R.id.listview);
		respondListView.setAdapter(new listAdapter());
	}

	public void dismiss() {
		if (mpopupwindow != null && mpopupwindow.isShowing()) {
			mpopupwindow.dismiss();
			mpopupwindow = null;

		}
	}

	public Boolean isShowing() {
		if (mpopupwindow != null) {

			return mpopupwindow.isShowing();
		} else {
			return false;
		}

	}

	class listAdapter extends BaseAdapter {
		public listAdapter() {

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return resonpList.size();
		}

		@Override
		public Object getItem(int i) {
			// TODO Auto-generated method stub
			return i;
		}

		@Override
		public long getItemId(int i) {
			// TODO Auto-generated method stub
			return i;
		}

		@Override
		public View getView(int i, View convertView, ViewGroup viewgroup) {
			// TODO Auto-generated method stub
			Holder childHolder = null;
			if (convertView == null) {
				childHolder = new Holder();
				convertView = mInflater.inflate(
						R.layout.respond_list_itemlayout, null);
				childHolder.phone = (TextView) convertView
						.findViewById(R.id.phoneTv);
				childHolder.isrespond = (TextView) convertView
						.findViewById(R.id.isRespond);
				convertView.setTag(childHolder);
			} else {
				childHolder = (Holder) convertView.getTag();
			}
			if (resonpList.get(i).get("type").equals("1")) {
				childHolder.phone.setText(resonpList.get(i).get("accept"));
				childHolder.isrespond.setText("接受");

			} else if (resonpList.get(i).get("type").equals("2")) {
				childHolder.phone.setText(resonpList.get(i).get("refuse"));
				childHolder.isrespond.setText("拒绝");
			} else {
				childHolder.phone.setText(resonpList.get(i).get("notRespond"));
				childHolder.isrespond.setText("未答复");
			}
			return convertView;
		}

		class Holder {
			TextView phone;
			TextView isrespond;
		}
	}
}
