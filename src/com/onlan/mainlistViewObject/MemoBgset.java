package com.onlan.mainlistViewObject;

import android.view.View;
import android.widget.ImageView;

import com.onlan.lazymemo.APPConfigure;
import com.onlan.lazymemo.R;

public class MemoBgset {
	public static void setBg(View v, int color, ImageView juan, Boolean isLeft) {
		v.setBackgroundColor(color);
		switch (color) {
		case APPConfigure.APP_Label_bg_defult:

			if (isLeft) {
				juan.setImageResource(R.drawable.labelbg_defult_left);
			} else {
				juan.setImageResource(R.drawable.labelbg_defult_right);
			}
			break;
		case APPConfigure.APP_Label_bg_1:

			if (isLeft) {
				juan.setImageResource(R.drawable.labelbg_1_left);
			} else {
				juan.setImageResource(R.drawable.labelbg_1_right);
			}
			break;
		case APPConfigure.APP_Label_bg_2:

			if (isLeft) {
				juan.setImageResource(R.drawable.labelbg_2_left);
			} else {
				juan.setImageResource(R.drawable.labelbg_2_right);
			}
			break;
		case APPConfigure.APP_Label_bg_3:

			if (isLeft) {
				juan.setImageResource(R.drawable.labelbg_3_left);
			} else {
				juan.setImageResource(R.drawable.labelbg_3_right);
			}
			break;
		case APPConfigure.APP_Label_bg_4:

			if (isLeft) {
				juan.setImageResource(R.drawable.labelbg_4_left);
			} else {
				juan.setImageResource(R.drawable.labelbg_4_right);
			}
			break;
		case APPConfigure.APP_Label_bg_5:

			if (isLeft) {
				juan.setImageResource(R.drawable.labelbg_5_left);
			} else {
				juan.setImageResource(R.drawable.labelbg_5_right);
			}
			break;
		case APPConfigure.APP_Label_bg_6:

			if (isLeft) {
				juan.setImageResource(R.drawable.labelbg_6_left);
			} else {
				juan.setImageResource(R.drawable.labelbg_6_right);
			}
			break;
		case APPConfigure.APP_Label_bg_7:

			if (isLeft) {
				juan.setImageResource(R.drawable.labelbg_7_left);
			} else {
				juan.setImageResource(R.drawable.labelbg_7_right);
			}
			break;
		case -1:
			setBg(v, APPConfigure.APP_Label_nowbg, juan, isLeft);
			break;
		default:
			break;
		}

	}
}
