package com.onlan.lazymemo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.onlan.lazymemo.Http.ClickAppVersion;
import com.onlan.myView.DialogTwoUtil;

@TargetApi(5)
public class SettingActivity extends Activity implements OnClickListener {
	private final String TAG = "SettingActivity";
	private ImageView Font_s;
	private ImageView Font_m;
	private ImageView Font_b;
	private RelativeLayout Fontbg_default;
	private RelativeLayout Fontbg_1;
	private RelativeLayout Fontbg_2;
	private RelativeLayout Fontbg_3;
	private RelativeLayout Fontbg_4;
	private RelativeLayout Fontbg_5;
	private RelativeLayout Fontbg_6;
	private RelativeLayout Fontbg_7;
	private TextView login;
	private TextView login_possin;

	private static SettingActivity mSettingActivity;

	public Boolean isUpdaterAPK = false;
	private String LonoutStr = "退出账号:";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting);
		MyApplication.addActivity(this);
		mSettingActivity = this;
		init();
		APPConfigure.setAPPBackGroup(null,
				(RelativeLayout) findViewById(R.id.SettingActivityID),
				APPConfigure.APPBgImgUrl, APPConfigure.APPBgImgID);
	}

	public static SettingActivity getSettingActivity() {
		return mSettingActivity;

	}

	private void init() {
		ImageView back = (ImageView) findViewById(R.id.left_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setResult(111);
				finish();
			}
		});
		Font_s = (ImageView) findViewById(R.id.font_s);
		Font_s.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				APPConfigure.savefontSize(APPConfigure.APP_Memo_FontSize1);
				selectFont(Font_s);
			}
		});
		Font_m = (ImageView) findViewById(R.id.font_m);
		Font_m.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				APPConfigure.savefontSize(APPConfigure.APP_Memo_FontSize2);
				selectFont(Font_m);
			}
		});
		Font_b = (ImageView) findViewById(R.id.font_b);
		Font_b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				APPConfigure.savefontSize(APPConfigure.APP_Memo_FontSize3);
				selectFont(Font_b);
			}
		});
		Fontbg_default = (RelativeLayout) findViewById(R.id.fontbg_defultbg);
		Fontbg_1 = (RelativeLayout) findViewById(R.id.fontbg_1bg);
		Fontbg_2 = (RelativeLayout) findViewById(R.id.fontbg_2bg);
		Fontbg_3 = (RelativeLayout) findViewById(R.id.fontbg_3bg);
		Fontbg_4 = (RelativeLayout) findViewById(R.id.fontbg_4bg);
		Fontbg_5 = (RelativeLayout) findViewById(R.id.fontbg_5bg);
		Fontbg_6 = (RelativeLayout) findViewById(R.id.fontbg_6bg);
		Fontbg_7 = (RelativeLayout) findViewById(R.id.fontbg_7bg);

		TextView OnUs = (TextView) findViewById(R.id.setting_onus);
		OnUs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(SettingActivity.this,
						AboutUsActivity.class), 13);
			}
		});
		TextView updater = (TextView) findViewById(R.id.setting_updater);
		updater.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isUpdaterAPK = true;
				ClickAppVersion.startClickAppVersion(SettingActivity.this, 1);
			}
		});
		login = (TextView) findViewById(R.id.setting_login);
		login_possin = (TextView) findViewById(R.id.setting_login_possion);
		Fontbg_default.setOnClickListener(this);
		Fontbg_1.setOnClickListener(this);
		Fontbg_2.setOnClickListener(this);
		Fontbg_3.setOnClickListener(this);
		Fontbg_4.setOnClickListener(this);
		Fontbg_5.setOnClickListener(this);
		Fontbg_6.setOnClickListener(this);
		Fontbg_7.setOnClickListener(this);

		RelativeLayout loginlayout = (RelativeLayout) findViewById(R.id.setting_loginlayout);
		loginlayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isUpdaterAPK = false;
				if (!APPConfigure.UserIsLogin) {
					startActivityForResult(new Intent(SettingActivity.this,
							LoginActivity.class), 12);
				} else {
					new DialogTwoUtil(SettingActivity.this, "您将要退出登录。是否确定？")
							.show();
				}
			}
		});
		if (APPConfigure.UserIsLogin) {
			login_possin.setVisibility(View.GONE);
			login.setText(LonoutStr + APPConfigure.APP_PhoneNum);
		}
		setnowFont();
		initFontBG();
	}

	public void lonOut() {
		APPConfigure.UserIsLogin = false;
		if (null != login_possin) {
			login_possin.setVisibility(View.VISIBLE);
		}
		if (null != login) {
			login.setText("一键登录");
		}
		APPConfigure.exit();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		setResult(111);
		finish();
		super.onBackPressed();
	}

	private void setnowFont() {
		switch (APPConfigure.APP_Memo_FontSize) {
		case 16:
			Font_s.setImageResource(R.drawable.zi_bg);
			Font_m.setImageResource(R.drawable.zi_unbg);
			Font_b.setImageResource(R.drawable.zi_unbg);
			break;
		case 21:
			Font_s.setImageResource(R.drawable.zi_unbg);
			Font_m.setImageResource(R.drawable.zi_bg);
			Font_b.setImageResource(R.drawable.zi_unbg);
			break;
		case 28:
			Font_s.setImageResource(R.drawable.zi_unbg);
			Font_m.setImageResource(R.drawable.zi_unbg);
			Font_b.setImageResource(R.drawable.zi_bg);
			break;
		default:
			break;
		}
	}

	private void selectFont(ImageView s) {
		switch (s.getId()) {
		case R.id.font_s:
			Font_s.setImageResource(R.drawable.zi_bg);
			Font_m.setImageResource(R.drawable.zi_unbg);
			Font_b.setImageResource(R.drawable.zi_unbg);
			break;
		case R.id.font_m:
			Font_s.setImageResource(R.drawable.zi_unbg);
			Font_m.setImageResource(R.drawable.zi_bg);
			Font_b.setImageResource(R.drawable.zi_unbg);
			break;
		case R.id.font_b:
			Font_s.setImageResource(R.drawable.zi_unbg);
			Font_m.setImageResource(R.drawable.zi_unbg);
			Font_b.setImageResource(R.drawable.zi_bg);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_setting, menu);
		return true;
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
			APPConfigure.saveFontBG(APPConfigure.APP_Label_bg_defult);
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
			APPConfigure.saveFontBG(APPConfigure.APP_Label_bg_1);
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
			APPConfigure.saveFontBG(APPConfigure.APP_Label_bg_2);
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
			APPConfigure.saveFontBG(APPConfigure.APP_Label_bg_3);
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
			APPConfigure.saveFontBG(APPConfigure.APP_Label_bg_4);
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
			APPConfigure.saveFontBG(APPConfigure.APP_Label_bg_5);
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
			APPConfigure.saveFontBG(APPConfigure.APP_Label_bg_6);
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
			APPConfigure.saveFontBG(APPConfigure.APP_Label_bg_7);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 132:
			APPConfigure.UserIsLogin = true;
			login_possin.setVisibility(View.GONE);
			login.setText(LonoutStr + APPConfigure.APP_PhoneNum);

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
}
