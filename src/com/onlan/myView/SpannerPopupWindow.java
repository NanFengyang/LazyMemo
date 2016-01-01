package com.onlan.myView;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.onlan.lazymemo.APPConfigure;
import com.onlan.lazymemo.MainActivity;
import com.onlan.lazymemo.R;

public class SpannerPopupWindow {
	private Activity mActivity;
	private PopupWindow mpopupwindow;
	private LinearLayout mTitlelayout;

	public SpannerPopupWindow(Activity activity) {
		this.mActivity = activity;
	}

	public void initmPopupWindowView(final LinearLayout mTitlelayout) {
		this.mTitlelayout = mTitlelayout;
		int w = mTitlelayout.getWidth();
		// // 获取自定义布局文件pop.xml的视图
		View customView = this.mActivity.getLayoutInflater().inflate(
				R.layout.popview_item, null, false);

		// 创建PopupWindow实例,200,150分别是宽度和高度
	
		if (APPConfigure.Screen_Width > 720) {
			mpopupwindow = new PopupWindow(customView, w,420);
		} else if(APPConfigure.Screen_Width == 720){
			mpopupwindow = new PopupWindow(customView, w, 280);
		}else{
			mpopupwindow = new PopupWindow(customView, w, 210);
		}
		mpopupwindow.setFocusable(true);
		// 设置允许在外点击消失
		mpopupwindow.setOutsideTouchable(true);
		// 刷新状态（必须刷新否则无效）
		mpopupwindow.update();
		mpopupwindow.setBackgroundDrawable(new BitmapDrawable());
		mpopupwindow.showAsDropDown(mTitlelayout, 0, -20);
		mpopupwindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				ImageView im = (ImageView) mTitlelayout
						.findViewById(R.id.title_img);
				im.setImageResource(R.drawable.icon_right);
			}
		});
		TextView history = (TextView) customView
				.findViewById(R.id.hietorymemoid);
		history.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (MainActivity.getMainActivity() != null) {
					MainActivity.getMainActivity().initHisMemo(true);
				}
				if (mpopupwindow != null && mpopupwindow.isShowing()) {
					mpopupwindow.dismiss();
					mpopupwindow = null;

				}
			}
		});
		TextView now = (TextView) customView.findViewById(R.id.nowmemoid);
		now.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (MainActivity.getMainActivity() != null) {
					MainActivity.getMainActivity().initNowMemo(true);
				}
				if (mpopupwindow != null && mpopupwindow.isShowing()) {
					mpopupwindow.dismiss();
					mpopupwindow = null;

				}
			}
		});
		TextView SendMemo = (TextView) customView
				.findViewById(R.id.searchememoid);
		SendMemo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (APPConfigure.UserIsLogin) {
					if (MainActivity.getMainActivity() != null) {
						MainActivity.getMainActivity().goSendMemo();
					}
				} else {
					new DialogUtil(mActivity, "登录之后才能查看哦。").show();
				}
				if (mpopupwindow != null && mpopupwindow.isShowing()) {
					mpopupwindow.dismiss();
					mpopupwindow = null;

				}
			}
		});
		TextView publicMemo = (TextView) customView
				.findViewById(R.id.publicmemoid);
		publicMemo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (APPConfigure.UserIsLogin) {
					if (MainActivity.getMainActivity() != null) {
						MainActivity.getMainActivity().gopublicMemo();
					}
				} else {
					new DialogUtil(mActivity, "登录之后才能查看哦。").show();
				}
				if (mpopupwindow != null && mpopupwindow.isShowing()) {
					mpopupwindow.dismiss();
					mpopupwindow = null;

				}
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
}
