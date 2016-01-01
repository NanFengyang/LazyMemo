package com.onlan.myView;

import java.util.ArrayList;
import java.util.List;
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
import android.widget.RadioButton;
import android.widget.TextView;
import com.onlan.lazymemo.APPConfigure;
import com.onlan.lazymemo.NewMemoActivity;
import com.onlan.lazymemo.R;

public class LocationAddressPopupWindow {
	private Activity mActivity;
	private PopupWindow mpopupwindow;

	private LayoutInflater mInflater;
	private List<String> contratsList;

	private ListView locationListView;
	private List<RadioButton> clickRadioButtonList;// 用于记录每个RadioButton的状态，并保证只可选一个

	public LocationAddressPopupWindow(Activity activity) {
		this.mActivity = activity;
		mInflater = LayoutInflater.from(activity);

	}

	public void initmPopupWindowView(final LinearLayout mTitlelayout,
			final List<String> contratsList) {
		this.contratsList = contratsList;
		int w = mTitlelayout.getWidth();
		// // 获取自定义布局文件pop.xml的视图
		View customView = this.mActivity.getLayoutInflater().inflate(
				R.layout.location_item, null, false);

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
		locationListView = (ListView) customView.findViewById(R.id.listview);
		locationListView.setFastScrollEnabled(true);
		locationListView.setAdapter(new listAdapter());
		TextView commit = (TextView) customView.findViewById(R.id.commit);
		commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (null != NewMemoActivity.getNewMemoActivity()) {
					if (clickRadioButtonList.size() > 0) {
						NewMemoActivity.getNewMemoActivity().setCityTag(
								contratsList.get(clickRadioButtonList.get(0)
										.getId()));
					} else {
						NewMemoActivity.getNewMemoActivity().setCityTag(
								contratsList.get(0));
					}

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

	class listAdapter extends BaseAdapter {
		private Holder childHolder = null;

		public listAdapter() {
			clickRadioButtonList = new ArrayList<RadioButton>();
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
				childHolder.address = (TextView) convertView
						.findViewById(R.id.phoneTv);
				childHolder.isclick = (RadioButton) convertView
						.findViewById(R.id.isclick);
				convertView.setTag(childHolder);
			} else {
				childHolder = (Holder) convertView.getTag();
			}
			String str = contratsList.get(i);
			childHolder.address.setText(str);
			childHolder.isclick.setId(i);
			childHolder.isclick.setTag("False");
			childHolder.isclick.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (v.getTag() == "False") {
						for (RadioButton rv : clickRadioButtonList) {
							rv.setChecked(false);
							rv.setTag("False");
						}
						clickRadioButtonList.clear();
						((RadioButton) v).setChecked(true);
						v.setTag("True");
						clickRadioButtonList.add((RadioButton) v);

					} else {
						((RadioButton) v).setChecked(false);
						v.setTag("False");
						clickRadioButtonList.clear();
					}

				}
			});
			return convertView;
		}

		class Holder {
			TextView address;
		
			RadioButton isclick;
		}
	}

}
