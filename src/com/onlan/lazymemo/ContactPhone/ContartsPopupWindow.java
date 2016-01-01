package com.onlan.lazymemo.ContactPhone;

import java.util.ArrayList;
import java.util.Collections;
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
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.onlan.lazymemo.APPConfigure;
import com.onlan.lazymemo.NewMemoActivity;
import com.onlan.lazymemo.R;

public class ContartsPopupWindow {
	private Activity mActivity;
	private PopupWindow mpopupwindow;
	private LinearLayout mTitlelayout;
	private IndexableListView respondListView;
	private LayoutInflater mInflater;
	private ArrayList<Map<String, String>> contratsList;
	private ArrayList<Map<String, String>> clickList;

	public ContartsPopupWindow(Activity activity) {
		this.mActivity = activity;
		mInflater = LayoutInflater.from(activity);

		clickList = new ArrayList<Map<String, String>>();
	}

	public void initmPopupWindowView(final LinearLayout mTitlelayout,
			ArrayList<Map<String, String>> contratsList) {
		this.contratsList = contratsList;
		this.mTitlelayout = mTitlelayout;
		int w = mTitlelayout.getWidth();
		// // 获取自定义布局文件pop.xml的视图
		View customView = this.mActivity.getLayoutInflater().inflate(
				R.layout.contarts_item, null, false);

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
		respondListView = (IndexableListView) customView
				.findViewById(R.id.listview);
		respondListView.setFastScrollEnabled(true);
		respondListView.setAdapter(new listAdapter());
		TextView commit = (TextView) customView.findViewById(R.id.commit);
		commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (null != NewMemoActivity.getNewMemoActivity()) {
					NewMemoActivity.getNewMemoActivity().setContarts(clickList);
				}
				dismiss();
			}
		});

		customView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
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

	class listAdapter extends BaseAdapter implements SectionIndexer {
		private Holder childHolder = null;
		private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		public listAdapter() {

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return contratsList.size();
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
		public View getView(final int i, View convertView, ViewGroup viewgroup) {
			// TODO Auto-generated method stub
			childHolder = null;
			if (convertView == null) {
				childHolder = new Holder();
				convertView = mInflater.inflate(
						R.layout.contarts_list_itemlayout, null);
				childHolder.phone = (TextView) convertView
						.findViewById(R.id.phoneTv);
				childHolder.isclick = (RadioButton) convertView
						.findViewById(R.id.isclick);
				convertView.setTag(childHolder);
			} else {
				childHolder = (Holder) convertView.getTag();
			}
			String str = contratsList.get(i).get("name") + "("
					+ contratsList.get(i).get("phone") + ")";
			childHolder.phone.setText(str);
			childHolder.isclick.setId(i);
			childHolder.isclick.setTag("False");
			childHolder.isclick.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (v.getTag() == "False") {
						((RadioButton) v).setChecked(true);
						v.setTag("True");
						clickList.add(contratsList.get(i));

					} else {
						((RadioButton) v).setChecked(false);
						v.setTag("False");
						int index = clickList.indexOf(contratsList.get(i));
						if (index != -1) {
							clickList.remove(index);
						}

					}
				}
			});
			return convertView;
		}

		class Holder {
			TextView phone;
			RadioButton isclick;
		}

		@Override
		public int getPositionForSection(int section) {
			// TODO Auto-generated method stub
			for (int i = section; i >= 0; i--) {
				for (int j = 0; j < getCount(); j++) {
					if (i == 0) {
						// For numeric section
						for (int k = 0; k <= 9; k++) {
							if (contratsList.get(j).get("sortKey") == null
									| contratsList.get(j).get("sortKey")
											.length() < 1) {
								return -1;
							}
							if (StringMatcher.match(
									String.valueOf(contratsList.get(j)
											.get("sortKey").charAt(0)),
									String.valueOf(k)))
								return j;
						}
					} else {
						if (contratsList.get(j).get("sortKey") == null
								| contratsList.get(j).get("sortKey").length() < 1) {
							return -1;
						}
						if (StringMatcher.match(
								String.valueOf(contratsList.get(j)
										.get("sortKey").charAt(0)),
								String.valueOf(mSections.charAt(i))))
							return j;
					}
				}
			}
			return -1;
		}

		@Override
		public int getSectionForPosition(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object[] getSections() {
			// TODO Auto-generated method stub
			String[] sections = new String[mSections.length()];
			for (int i = 0; i < mSections.length(); i++)
				sections[i] = String.valueOf(mSections.charAt(i));
			return sections;
		}
	}

}
