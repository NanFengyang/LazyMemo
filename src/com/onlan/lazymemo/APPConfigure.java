package com.onlan.lazymemo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.onlan.myView.DialogUtil;
import com.onlna.lazymemo.ImageDown.ImageDownLoader;

public class APPConfigure {
	public static Boolean isFristGo = true;

	public final static String Version = "V1.0.6";// 用户显示给用户看

	public final static String platformId = "2";

	public final static String versionNum = "4";

	public static int Screen_Width = 720;

	public static int Screen_Hight = 1280;



	/**
	 * app下载图片保存在本地的目录
	 */
	public static final String APP_SAVEAPPBG_Catalog = Environment
			.getExternalStorageDirectory() + "/memo_note/app_bg/";

	/**
	 * APP用户是否已经登录
	 */
	public static Boolean UserIsLogin = false;

	/**
	 * 全局dialog，默认显示文字
	 */
	public static String DialogTextShow = "正在努力";

	/**
	 * 全局Token和UserId
	 * 
	 */
	public static String APP_Token = "";
	public static String APP_PhoneNum = "";
	/**
	 * app全局字体设置；默认最小 16.
	 */

	public static int APP_Memo_FontSize1 = 16;
	public static int APP_Memo_FontSize2 = 21;
	public static int APP_Memo_FontSize3 = 28;
	public static int APP_Memo_FontSize = APP_Memo_FontSize1;

	/**
	 * APP全局标签背景颜色
	 */

	public static final int APP_Label_bg_1 = 0xfff20d94;
	public static final int APP_Label_bg_2 = 0xffd805c1;
	public static final int APP_Label_bg_3 = 0xffb684ff;
	public static final int APP_Label_bg_4 = 0xff1a9ef8;
	public static final int APP_Label_bg_5 = 0xffffd500;
	public static final int APP_Label_bg_6 = 0xff65c400;
	public static final int APP_Label_bg_7 = 0xff00daea;
	public static final int APP_Label_bg_defult = 0xffe3a999;

	public static int APP_Label_nowbg = APP_Label_bg_defult;

	/**
	 * 获取APP装机设置
	 */
	public static Boolean isAppInstall() {
		SharedPreferences userDetails = MyApplication.getMyApplication()
				.getSharedPreferences("isInstall", Context.MODE_PRIVATE);

		return userDetails.getBoolean("isInstall", false);
	}

	/**
	 * 保存APP装机设置
	 */
	public static void saveAppInstall(Boolean isInstall) {
		SharedPreferences userDetails = MyApplication.getMyApplication()
				.getSharedPreferences("isInstall", Context.MODE_PRIVATE);
		Editor edit = userDetails.edit();
		edit.clear();
		edit.putBoolean("isInstall", isInstall);

		edit.commit();
	}

	/**
	 * 保存字体背景
	 */
	public static void saveFontBG(int fonsize) {
		SharedPreferences userDetails = MyApplication.getMyApplication()
				.getSharedPreferences("app_font_bg", Context.MODE_PRIVATE);
		Editor edit = userDetails.edit();
		edit.clear();
		edit.putInt("app_fontbg", fonsize);
		edit.commit();
		APP_Label_nowbg = fonsize;
	}

	/**
	 * 读取上一次存储字体背景
	 */
	public static int getAPPFontBG() {
		SharedPreferences userDetails = MyApplication.getMyApplication()
				.getSharedPreferences("app_font_bg", Context.MODE_PRIVATE);
		return APP_Label_nowbg = userDetails.getInt("app_fontbg",
				APP_Label_bg_defult);

	}

	/**
	 * 保存字体大小
	 */
	public static void savefontSize(int fonsize) {
		SharedPreferences userDetails = MyApplication.getMyApplication()
				.getSharedPreferences("app_setting", Context.MODE_PRIVATE);
		Editor edit = userDetails.edit();
		edit.clear();
		edit.putInt("app_fontsize", fonsize);
		edit.commit();
		APP_Memo_FontSize = fonsize;
	}

	/**
	 * 读取上一次存储字体大小
	 */
	public static int getAPPfontsize() {
		SharedPreferences userDetails = MyApplication.getMyApplication()
				.getSharedPreferences("app_setting", Context.MODE_PRIVATE);
		return APP_Memo_FontSize = userDetails.getInt("app_fontsize",
				APP_Memo_FontSize2);

	}

	/**
	 * 保存Token
	 */
	public static void saveToken(String tonken, String userId) {
		SharedPreferences userDetails = MyApplication.getMyApplication()
				.getSharedPreferences("app_token", Context.MODE_PRIVATE);
		Editor edit = userDetails.edit();
		edit.clear();
		edit.putString("app_Token", tonken);
		edit.putString("app_userId", userId);
		edit.commit();
		APPConfigure.APP_Token = tonken;
		APPConfigure.APP_PhoneNum = userId;
	}

	/**
	 * 读取上一次存储Token
	 */
	public static void getAPPToken() {
		SharedPreferences userDetails = MyApplication.getMyApplication()
				.getSharedPreferences("app_token", Context.MODE_PRIVATE);
		APPConfigure.APP_Token = userDetails.getString("app_Token", "");
		APPConfigure.APP_PhoneNum = userDetails.getString("app_userId", "");
	}

	/**
	 * 清理缓存
	 */
	public static void exit() {
		APP_Token = "";
		APP_PhoneNum = "";
		SharedPreferences userDetails1 = MyApplication.getMyApplication()
				.getSharedPreferences("app_token", Context.MODE_PRIVATE);
		Editor edit1 = userDetails1.edit();
		edit1.clear();
		edit1.commit();
		SharedPreferences userDetails2 = MyApplication.getMyApplication()
				.getSharedPreferences("app_setting", Context.MODE_PRIVATE);
		Editor edit2 = userDetails2.edit();
		edit2.clear();
		edit2.commit();
		SharedPreferences userDetails3 = MyApplication.getMyApplication()
				.getSharedPreferences("app_font_bg", Context.MODE_PRIVATE);
		Editor edit3 = userDetails3.edit();
		edit3.clear();
		edit3.commit();

	}

	/**
	 * APP背景图片本地ID或者路径
	 */
	public static int APPBgImgID = -1;

	public static String APPBgImgUrl = null;

	/**
	 * 修改APP每一个页面的背景图片。
	 * 
	 * @param mList
	 * @param path
	 * @param imgSourseId
	 */
	private static Bitmap bitmap = null;

	public static void setAPPBackGroup(final LinearLayout mLinearLayout,
			final RelativeLayout mRelativeLayout, String path, int imgSourseId) {

		try {
			if (null != path && path.length() > 0 && null == bitmap) {

				ImageDownLoader mImageDownLoader = new ImageDownLoader(
						MyApplication.getMyApplication());
				mImageDownLoader.loadImage(path, Screen_Width, Screen_Hight,
						new ImageDownLoader.AsyncImageLoaderListener() {

							@Override
							public void onImageLoader(Bitmap mbitmap) {
								// TODO Auto-generated method stub
								bitmap = mbitmap;
								if (null != mLinearLayout && null != mbitmap) {
									mLinearLayout
											.setBackgroundDrawable(new BitmapDrawable(
													mbitmap));
								}
								if (null != mRelativeLayout && null != mbitmap) {
									mRelativeLayout
											.setBackgroundDrawable(new BitmapDrawable(
													mbitmap));
								}
							}
						});

			} else {
				if (null != bitmap) {
					if (null != mLinearLayout && null != bitmap) {
						mLinearLayout.setBackgroundDrawable(new BitmapDrawable(
								bitmap));

					}
					if (null != mRelativeLayout && null != bitmap) {
						mRelativeLayout
								.setBackgroundDrawable(new BitmapDrawable(
										bitmap));

					}
				}
			}

			if (-1 != imgSourseId) {
				if (null != mLinearLayout) {
					mLinearLayout.setBackgroundResource(imgSourseId);

				}
				if (null != mRelativeLayout) {
					mRelativeLayout.setBackgroundResource(imgSourseId);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void CheckToken(Activity mActivity, int resultId,
			String resultMSG) {
		if (resultId == 6000) {
			new DialogUtil(mActivity, "登录失效，请重新登录！").show();
			APP_Token = "";
			APP_PhoneNum = "";
			UserIsLogin = false;
		} else {
			new DialogUtil(mActivity, resultMSG).show();
		}

	}

}
